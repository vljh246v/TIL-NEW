"""
추천 시스템 알고리즘 구현 모듈
"""

import numpy as np
import pandas as pd
from collections import defaultdict
from typing import Dict, List
from sklearn.metrics.pairwise import cosine_similarity

from utils.models import Dataset, RecommendResult

# =========================
# 기본 추천 시스템 인터페이스
# =========================

class BaseRecommender:
    """
    모든 추천 시스템이 상속받는 기본 클래스
    """
    def recommend(self, dataset: Dataset, **kwargs) -> RecommendResult:
        """
        추천 알고리즘의 기본 인터페이스.
        모든 추천 모델은 이 메서드를 구현해야 함.

        Args:
            dataset: 훈련 및 테스트 데이터를 포함하는 데이터셋
            **kwargs: 추가 매개변수

        Returns:
            RecommendResult: 평점 예측 및 추천 아이템 목록
        """
        raise NotImplementedError

# =========================
# 유틸리티 함수
# =========================

def recommend_sorted(scores: Dict[int,float], seen: set, k: int) -> List[int]:
    """
    예측 점수를 기반으로 상위 k개 아이템을 추천 목록으로 생성하는 유틸리티 함수

    1. 사용자가 아직 평가하지 않은 아이템 중에서 (seen에 없는 아이템)
    2. 예측 점수가 높은 순서로 정렬하여 상위 k개 선택 (동일 점수는 아이템 ID 오름차순)

    Args:
        scores: {아이템 ID: 예측 점수} 형태의 딕셔너리
        seen: 사용자가 이미 평가한 아이템 ID 집합
        k: 추천할 아이템 개수

    Returns:
        추천할 상위 k개 아이템 ID 리스트
    """
    # (-score, movieId asc) 형태로 정렬
    cands = ((m, s) for m, s in scores.items() if m not in seen)
    return [m for m,_ in sorted(cands, key=lambda x: (-x[1], x[0]))[:k]]

# =========================
# 베이스라인 추천 시스템
# =========================

class GlobalMeanRecommender(BaseRecommender):
    """전체 평균 기반 추천 시스템 (Baseline)

    가장 단순한 형태의 추천 시스템으로, 모든 예측값을 전체 평점의 평균으로 설정합니다.
    실제 추천 목록은 생성하지 않으며, 이는 다른 알고리즘의 성능을 비교하는 기준선으로 사용됩니다.

    알고리즘 로직:
    1. 훈련 데이터의 모든 평점 평균(μ)을 계산합니다
    2. 모든 테스트 데이터의 예측값을 이 평균으로 설정합니다
    3. 예측값을 0.5~5.0 범위로 제한합니다
    4. 실제 추천 아이템 목록은 빈 리스트를 반환합니다
    """
    def recommend(self, dataset: Dataset, **kwargs) -> RecommendResult:
        mu = float(dataset.train['rating'].mean())
        pred = pd.Series(mu, index=dataset.test.index).clip(lower=0.5, upper=5.0)
        return RecommendResult(pred, defaultdict(list))

class PopularityRecommender(BaseRecommender):
    """인기도 기반 추천 시스템

    가장 많은 사용자가 평가한(인기 있는) 아이템을 추천하는 방식으로,
    협업 필터링의 가장 단순한 형태입니다. 아이템 평가 횟수를 기준으로 인기도를 측정합니다.

    알고리즘 로직:
    1. RMSE 평가를 위한 평점 예측:
       - 각 영화별 평균 평점을 계산합니다
       - 테스트 데이터의 영화 ID에 맞는 평균 평점을 예측값으로 사용합니다
       - 테스트 데이터에 없는 영화는 전체 평균(μ)으로 설정합니다
       - 예측값을 0.5~5.0 범위로 제한합니다

    2. 추천 목록 생성:
       - 영화별로 평가 횟수(cnt)와 평균 평점(mean)을 계산합니다
       - minimum_num_rating 이상 평가받은 영화만 고려합니다
       - 평가 횟수 기준 내림차순, 동률 시 영화 ID 기준 오름차순으로 정렬합니다
       - 각 사용자가 이미 평가한 영화를 제외한 상위 k개 영화를 추천합니다

    매개변수:
    - k: 추천 목록 크기 (기본값: 10)
    - minimum_num_rating: 최소 평가 횟수 기준 (기본값: 0)
    """
    def recommend(self, dataset: Dataset, k=10, minimum_num_rating=0, **kwargs) -> RecommendResult:
        train, test = dataset.train, dataset.test
        mu = float(train['rating'].mean())

        # RMSE용: 영화별 평균 → 없는 영화 μ → clip
        movie_mean = train.groupby('movieId')['rating'].mean().rename('rating_pred')
        pred = test[['movieId']].merge(movie_mean, on='movieId', how='left')['rating_pred'].fillna(mu)
        pred = pd.Series(pred.values, index=test.index).clip(lower=0.5, upper=5.0)

        # 인기순: cnt desc, tie → movieId asc
        stats = (train.groupby('movieId')['rating'].agg(cnt='count', mean='mean').reset_index())
        stats = stats[stats['cnt'] >= minimum_num_rating]
        stats = stats.sort_values(['cnt','movieId'], ascending=[False, True])
        popular = stats['movieId'].tolist()

        seen = train.groupby('userId')['movieId'].apply(set).to_dict()
        user2items = defaultdict(list)
        for u in test['userId'].unique():
            s = seen.get(u, set())
            rec = [m for m in popular if m not in s][:k]
            user2items[u] = rec

        return RecommendResult(pred, user2items)

# =========================
# 협업 필터링 추천 시스템
# =========================

class UserKNNRecommender(BaseRecommender):
    """사용자 기반 협업 필터링 (User-based Collaborative Filtering)

    유사한 사용자들이 좋아한 아이템을 추천하는 방식입니다.
    사용자-아이템 평점 행렬에서 사용자 간 유사도를 계산하고,
    타겟 사용자와 유사한 사용자들의 평점을 가중평균하여 예측합니다.

    알고리즘 로직:
    1. 사용자-아이템 행렬 생성:
       - 행: 사용자 ID, 열: 영화 ID, 값: 평점
       - 평가하지 않은 항목은 0으로 채움

    2. 사용자 간 코사인 유사도 계산:
       - 각 사용자 벡터 간의 코사인 유사도를 계산
       - 유사도가 높을수록 취향이 비슷함을 의미

    3. 평점 예측 (RMSE 계산용):
       - 각 테스트 아이템에 대해 대상 사용자와 유사한 n_neighbors명의 이웃을 선택
       - 이웃 중 해당 아이템을 평가한 사용자의 평점을 유사도로 가중평균하여 예측
       - 이웃 중 아무도 해당 아이템을 평가하지 않았다면 전체 평균(μ) 사용
       - 예측값을 0.5~5.0 범위로 제한

    4. 아이템 추천:
       - 각 사용자가 아직 평가하지 않은 모든 아이템에 대해 예측 평점을 계산
       - 예측 평점이 높은 순으로 상위 k개 아이템을 추천

    매개변수:
    - k: 추천 목록 크기 (기본값: 10)
    - n_neighbors: 유사도 계산에 사용할 이웃 수 (기본값: 20)
    """
    def recommend(self, dataset: Dataset, k=10, n_neighbors=20, **kwargs) -> RecommendResult:
        train, test = dataset.train, dataset.test
        mu = float(train['rating'].mean())  # 전체 평점 평균

        # 사용자-아이템 행렬 생성 (ui: User-Item matrix)
        ui = train.pivot_table(index='userId', columns='movieId', values='rating').fillna(0.0)
        # 사용자 간 코사인 유사도 계산
        sim = cosine_similarity(ui)
        sim_df = pd.DataFrame(sim, index=ui.index, columns=ui.index)
        # 사용자별 이미 평가한 아이템 목록
        seen = train.groupby('userId')['movieId'].apply(set).to_dict()

        # RMSE 계산을 위한 평점 예측
        pred = pd.Series(index=test.index, dtype=float)
        for idx, row in test.iterrows():
            u, i = row['userId'], row['movieId']  # 사용자 ID와 영화 ID
            # 학습 데이터에 없는 사용자나 아이템은 전체 평균 사용
            if (u not in ui.index) or (i not in ui.columns):
                pred.at[idx] = mu; continue
            # 자신을 제외한 가장 유사한 n_neighbors명의 이웃 선택
            neigh = sim_df.loc[u].sort_values(ascending=False).iloc[1:n_neighbors+1]
            # 선택된 이웃들의 해당 영화 평점
            neigh_r = ui.loc[neigh.index, i]
            # 해당 영화를 평가한 이웃만 선택 (평점 > 0)
            mask = neigh_r > 0
            if mask.sum()==0: pred.at[idx]=mu  # 평가한 이웃이 없으면 전체 평균 사용
            else:
                # 유사도를 가중치로 하여 평점 가중평균 계산
                w = neigh[mask]; r = neigh_r[mask]; s=w.sum()
                pred.at[idx] = float((w*r).sum()/s) if s>1e-8 else mu
        # 예측값 범위 조정 (0.5~5.0)
        pred = pred.clip(lower=0.5, upper=5.0)

        # 추천 목록 생성
        user2items = defaultdict(list)
        for u in test['userId'].unique():
            # 학습 데이터에 없는 사용자는 모든 영화에 평균 점수 부여
            if u not in ui.index:
                scores = {m: mu for m in ui.columns}
            else:
                # 아직 평가하지 않은 영화 목록
                unseen = [m for m in ui.columns if m not in seen.get(u,set())]
                # 가장 유사한 이웃 선택
                neigh = sim_df.loc[u].sort_values(ascending=False).iloc[1:n_neighbors+1]
                scores = {}
                # 각 영화마다 예측 점수 계산
                for i in unseen:
                    neigh_r = ui.loc[neigh.index, i]
                    mask = neigh_r > 0
                    if mask.sum()==0: scores[i]=mu  # 평가한 이웃이 없으면 전체 평균 사용
                    else:
                        # 유사도 가중 평점 계산
                        w=neigh[mask]; r=neigh_r[mask]; s=w.sum()
                        scores[i]=float((w*r).sum()/s) if s>1e-8 else mu
            # 점수가 높은 순서로 상위 k개 영화 추천
            user2items[u] = recommend_sorted(scores, seen.get(u,set()), k)
        return RecommendResult(pred, user2items)

class ItemKNNRecommender(BaseRecommender):
    """아이템 기반 협업 필터링 (Item-based Collaborative Filtering)

    사용자가 평가한 아이템과 유사한 다른 아이템을 추천하는 방식입니다.
    아이템 간 유사도를 계산하고, 사용자가 높이 평가한 아이템과 유사한 아이템을 추천합니다.

    알고리즘 로직:
    1. 사용자-아이템 행렬 생성 및 전치:
       - 원본: 행=사용자, 열=영화
       - 전치(iu): 행=영화, 열=사용자 (Item-User matrix)

    2. 아이템 간 코사인 유사도 계산:
       - 각 영화 벡터 간의 코사인 유사도를 계산
       - 유사도가 높을수록 비슷한 영화임을 의미

    3. 평점 예측 (RMSE 계산용):
       - 예측할 영화와 가장 유사한 n_neighbors개의 영화를 선택
       - 그 중 사용자가 평가한 영화들만 고려(common)
       - 유사도를 가중치로 하여 사용자의 평점을 가중평균
       - 유사한 영화를 사용자가 평가하지 않았다면 전체 평균(μ) 사용
       - 예측값을 0.5~5.0 범위로 제한

    4. 아이템 추천:
       - 각 사용자가 아직 평가하지 않은 모든 영화에 대해 예측 평점 계산
       - 예측 평점이 높은 순으로 상위 k개 영화를 추천

    매개변수:
    - k: 추천 목록 크기 (기본값: 10)
    - n_neighbors: 유사도 계산에 사용할 이웃 수 (기본값: 20)

    특징:
    - 사용자 기반보다 일반적으로 더 안정적인 성능을 보임
    - 아이템 간 유사도는 변화가 적어 미리 계산해두면 효율적
    - Cold-start 문제에 비교적 강건함
    """
    def recommend(self, dataset: Dataset, k=10, n_neighbors=20, **kwargs) -> RecommendResult:
        train, test = dataset.train, dataset.test
        mu = float(train['rating'].mean())  # 전체 평점 평균

        # 사용자-아이템 행렬 생성 및 전치하여 아이템-사용자 행렬로 변환
        ui = train.pivot_table(index='userId', columns='movieId', values='rating').fillna(0.0)
        iu = ui.T  # 전치: 행=영화, 열=사용자
        # 아이템 간 코사인 유사도 계산
        sim = cosine_similarity(iu)
        sim_df = pd.DataFrame(sim, index=iu.index, columns=iu.index)
        # 사용자별 이미 평가한 아이템 목록
        seen = train.groupby('userId')['movieId'].apply(set).to_dict()

        # RMSE 계산을 위한 평점 예측
        pred = pd.Series(index=test.index, dtype=float)
        for idx, row in test.iterrows():
            u, i = row['userId'], row['movieId']  # 사용자 ID와 영화 ID
            # 학습 데이터에 없는 사용자나 아이템은 전체 평균 사용
            if (u not in ui.index) or (i not in ui.columns):
                pred.at[idx] = mu; continue
            # 현재 영화와 가장 유사한 n_neighbors개의 영화 선택
            sims = sim_df[i].sort_values(ascending=False).iloc[1:n_neighbors+1]
            # 현재 사용자가 평가한 영화들
            rated = ui.loc[u]
            # 유사한 영화 중 사용자가 평가한 영화만 선택
            common = sims.index[rated.loc[sims.index] > 0]
            if len(common)==0: pred.at[idx]=mu  # 공통 영화가 없으면 전체 평균 사용
            else:
                # 유사도를 가중치로 하여 평점 가중평균 계산
                w=sims.loc[common]; r=rated.loc[common]; s=w.sum()
                pred.at[idx]=float((w*r).sum()/s) if s>1e-8 else mu
        # 예측값 범위 조정 (0.5~5.0)
        pred = pred.clip(lower=0.5, upper=5.0)

        # 추천 목록 생성
        user2items = defaultdict(list)
        for u in test['userId'].unique():
            # 학습 데이터에 없는 사용자는 모든 영화에 평균 점수 부여
            if u not in ui.index:
                scores = {m: mu for m in ui.columns}
            else:
                # 아직 평가하지 않은 영화 목록
                unseen = [m for m in ui.columns if m not in seen.get(u,set())]
                # 해당 사용자가 평가한 영화들
                rated = ui.loc[u]
                scores = {}
                # 각 미평가 영화에 대해 예측 점수 계산
                for i in unseen:
                    # 현재 영화와 가장 유사한 n_neighbors개의 영화 선택
                    sims = sim_df[i].sort_values(ascending=False).iloc[1:n_neighbors+1]
                    # 유사한 영화 중 사용자가 평가한 영화만 선택
                    common = sims.index[rated.loc[sims.index] > 0]
                    if len(common)==0: scores[i]=mu  # 공통 영화가 없으면 전체 평균 사용
                    else:
                        # 유사도 가중 평점 계산
                        w=sims.loc[common]; r=rated.loc[common]; s=w.sum()
                        scores[i]=float((w*r).sum()/s) if s>1e-8 else mu
            # 점수가 높은 순서로 상위 k개 영화 추천
            user2items[u] = recommend_sorted(scores, seen.get(u,set()), k)
        return RecommendResult(pred, user2items)

# =========================
# 행렬 분해 기반 추천 시스템
# =========================

class MFRecommender(BaseRecommender):
    """행렬 분해(Matrix Factorization) 기반 추천 시스템

    사용자-아이템 평점 행렬을 저차원의 잠재 요인(latent factor) 행렬로 분해하여
    누락된 평점을 예측하는 방식입니다. 구현된 알고리즘은 SGD(Stochastic Gradient Descent)를
    사용하여 최적화하는 기본적인 행렬 분해 모델입니다.

    모델 수식:
    예측 평점 r̂_ui = μ + bu + bi + p_u · q_i^T
    여기서:
    - μ: 전체 평균 평점
    - bu: 사용자 u의 편향(bias)
    - bi: 아이템 i의 편향(bias)
    - p_u: 사용자 u의 잠재 요인 벡터
    - q_i: 아이템 i의 잠재 요인 벡터

    알고리즘 로직:
    1. 전처리:
       - 사용자 ID와 영화 ID를 내부 인덱스로 매핑
       - 전체 평균(μ) 계산
       - 사용자 요인 행렬(P), 아이템 요인 행렬(Q) 초기화
       - 사용자 편향(bu), 아이템 편향(bi) 초기화

    2. SGD 학습:
       - 모든 학습 데이터에 대해 n_epochs 횟수만큼 반복
       - 각 평점에 대해:
         a. 현재 모델로 평점 예측
         b. 오차(e) 계산
         c. 경사하강법으로 파라미터 업데이트:
            - 사용자 편향(bu) 업데이트
            - 아이템 편향(bi) 업데이트
            - 사용자 요인 벡터(P[u]) 업데이트
            - 아이템 요인 벡터(Q[i]) 업데이트

    3. 평점 예측 (RMSE 계산용):
       - 테스트 데이터의 각 항목에 대해 학습된 모델로 평점 예측
       - 예측값을 0.5~5.0 범위로 제한

    4. 아이템 추천:
       - 각 사용자에 대해 모든 아이템의 예측 평점 계산
       - 이미 평가한 아이템을 제외하고 예측 평점이 높은 상위 k개 추천

    매개변수:
    - k: 추천 목록 크기 (기본값: 10)
    - n_factors: 잠재 요인의 차원 수 (기본값: 20)
    - learning_rate: 학습률 (기본값: 0.01)
    - n_epochs: 반복 학습 횟수 (기본값: 50)
    - reg: 정규화 계수 (기본값: 0.08)

    특징:
    - 메모리 기반 협업 필터링(UserKNN, ItemKNN)보다 일반적으로 더 높은 정확도
    - 대용량 데이터에서도 효율적으로 학습 가능
    - 사용자와 아이템의 잠재적 특성을 학습하여 데이터 희소성 문제를 완화
    """
    def recommend(self, dataset: Dataset, k=10, **kwargs) -> RecommendResult:
        np.random.seed(0)  # 재현성을 위한 시드 고정
        # 하이퍼파라미터 설정
        n_factors = kwargs.get('n_factors', 20)  # 잠재 요인 차원
        lr = kwargs.get('learning_rate', 0.01)  # 학습률
        n_epochs = kwargs.get('n_epochs', 50)   # 반복 학습 횟수
        reg = kwargs.get('reg', 0.08)           # 정규화 계수

        train, test = dataset.train, dataset.test
        mu = float(train['rating'].mean())  # 전체 평균 평점

        # ID를 내부 인덱스로 매핑
        users = sorted(train.userId.unique())
        items = sorted(train.movieId.unique())
        uid2i = {u:i for i,u in enumerate(users)}  # 사용자 ID → 인덱스
        iid2j = {m:j for j,m in enumerate(items)}  # 영화 ID → 인덱스
        nU, nI = len(users), len(items)  # 사용자 수, 아이템 수

        # 모델 파라미터 초기화
        P = 0.1*np.random.randn(nU, n_factors)  # 사용자 요인 행렬
        Q = 0.1*np.random.randn(nI, n_factors)  # 아이템 요인 행렬
        bu = np.zeros(nU)  # 사용자 편향
        bi = np.zeros(nI)  # 아이템 편향

        # 학습 데이터 인덱스 매핑
        df = train.copy()
        df['ui'] = df['userId'].map(uid2i)  # 사용자 ID → 인덱스
        df['ij'] = df['movieId'].map(iid2j)  # 영화 ID → 인덱스

        # SGD로 모델 학습
        for _ in range(n_epochs):
            for r in df.itertuples(index=False):
                u, j, y = int(r.ui), int(r.ij), float(r.rating)
                # 예측값 계산: μ + bu + bi + p_u·q_i^T
                pred = mu + bu[u] + bi[j] + P[u].dot(Q[j])
                # 오차 계산
                e = y - pred
                # 파라미터 업데이트
                bu[u] += lr*(e - reg*bu[u])  # 사용자 편향 업데이트
                bi[j] += lr*(e - reg*bi[j])   # 아이템 편향 업데이트
                Pu = P[u].copy(); Qj = Q[j].copy()  # 원본 보존
                P[u] += lr*(e*Qj - reg*Pu)   # 사용자 요인 업데이트
                Q[j] += lr*(e*Pu - reg*Qj)   # 아이템 요인 업데이트

        # RMSE 계산을 위한 평점 예측
        pred = pd.Series(index=test.index, dtype=float)
        for idx, row in test.iterrows():
            u, m = row['userId'], row['movieId']  # 사용자 ID와 영화 ID
            # 학습 데이터에 없는 사용자나 아이템은 전체 평균 사용
            if (u not in uid2i) or (m not in iid2j):
                pred.at[idx] = mu
            else:
                # 인덱스 매핑
                ui = uid2i[u]; ij = iid2j[m]
                # 예측값 계산: μ + bu + bi + p_u·q_i^T
                pred.at[idx] = mu + bu[ui] + bi[ij] + P[ui].dot(Q[ij])
        # 예측값 범위 조정 (0.5~5.0)
        pred = pred.clip(lower=0.5, upper=5.0)

        # 추천 목록 생성
        seen = train.groupby('userId')['movieId'].apply(set).to_dict()
        user2items = defaultdict(list)
        for u in test['userId'].unique():
            # 학습 데이터에 없는 사용자는 모든 아이템에 평균 점수 부여
            if u not in uid2i:
                scores = {m: mu for m in items}
            else:
                # 모든 아이템에 대한 예측 점수를 한 번에 계산
                ui = uid2i[u]
                s = mu + bu[ui] + bi + Q @ P[ui]  # 행렬 곱을 통한 벡터화 연산
                scores = {items[j]: float(s[j]) for j in range(nI)}
            # 점수가 높은 순서로 상위 k개 아이템 추천
            user2items[u] = recommend_sorted(scores, seen.get(u,set()), k)

        return RecommendResult(pred, user2items)