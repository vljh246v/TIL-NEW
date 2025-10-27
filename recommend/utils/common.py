"""
추천 시스템 구현을 위한 공통 유틸리티 모듈
"""

import dataclasses
import numpy as np
import pandas as pd
from dataclasses import dataclass
from typing import Dict, List, Optional
from collections import defaultdict
from sklearn.metrics import mean_squared_error

# =========================
# 데이터 및 결과 클래스
# =========================

@dataclass(frozen=True)
class Dataset:
    """
    추천 시스템에 사용되는 데이터셋 구조
    """
    train: pd.DataFrame
    test: pd.DataFrame
    test_user2items: Dict[int, List[int]]
    item_content: pd.DataFrame

@dataclass(frozen=True)
class RecommendResult:
    """
    추천 시스템의 결과를 저장하는 클래스
    """
    rating: pd.Series                  # test 인덱스와 정렬을 맞춘 예측값
    user2items: Dict[int, List[int]]   # 추천 Top-K 목록

@dataclass(frozen=True)
class Metrics:
    """
    추천 시스템의 성능 평가 메트릭스
    """
    rmse: float
    precision_at_k: float
    recall_at_k: float
    params: Dict = dataclasses.field(default_factory=dict)
    def __repr__(self):
        return f"rmse={self.rmse:.3f}, Precision@K={self.precision_at_k:.3f}, Recall@K={self.recall_at_k:.3f}, Params={self.params}"

# =========================
# 추천 시스템 기본 클래스
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
# 평가 유틸리티
# =========================

class MetricCalculator:
    """
    추천 시스템 성능 평가를 위한 메트릭스 계산기
    """
    def calc(self, true_rating: List[float], pred_rating: List[float],
             true_user2items: Dict[int, List[int]],
             pred_user2items: Dict[int, List[int]], k: int, params: Dict=None) -> Metrics:
        """
        추천 시스템의 성능을 평가하는 메트릭스 계산

        Args:
            true_rating: 실제 평점 리스트
            pred_rating: 예측 평점 리스트
            true_user2items: 실제로 사용자가 높게 평가한 아이템 {사용자ID: [아이템ID 리스트]}
            pred_user2items: 추천된 아이템 {사용자ID: [추천 아이템ID 리스트]}
            k: Top-K 추천 목록 크기
            params: 추가 파라미터 (모델명 등)

        Returns:
            Metrics: 성능 평가 메트릭스
        """
        rmse = self._calc_rmse(true_rating, pred_rating)
        p = self._calc_precision_at_k(true_user2items, pred_user2items, k)
        r = self._calc_recall_at_k(true_user2items, pred_user2items, k)
        return Metrics(rmse, p, r, params or {})

    def _calc_rmse(self, y, yhat) -> float:
        """
        실제 평점과 예측 평점 사이의 Root Mean Squared Error를 계산
        """
        if not y or not yhat: return np.nan
        return float(np.sqrt(mean_squared_error(y, yhat)))

    def _precision_at_k(self, true_items: List[int], pred_items: List[int], k: int) -> float:
        """
        단일 사용자에 대한 Precision@K 계산
        """
        if k == 0: return 0.0
        return len(set(true_items) & set(pred_items[:k])) / k

    def _recall_at_k(self, true_items: List[int], pred_items: List[int], k: int) -> float:
        """
        단일 사용자에 대한 Recall@K 계산
        """
        if len(true_items) == 0 or k == 0: return 0.0
        return len(set(true_items) & set(pred_items[:k])) / len(true_items)

    def _calc_precision_at_k(self, true_u2i, pred_u2i, k):
        """
        모든 사용자에 대한 평균 Precision@K 계산
        """
        scores = []
        for u in true_u2i.keys():
            scores.append(self._precision_at_k(true_u2i[u], pred_u2i.get(u, []), k))
        return float(np.mean(scores)) if scores else 0.0

    def _calc_recall_at_k(self, true_u2i, pred_u2i, k):
        """
        모든 사용자에 대한 평균 Recall@K 계산
        """
        scores = []
        for u in true_u2i.keys():
            scores.append(self._recall_at_k(true_u2i[u], pred_u2i.get(u, []), k))
        return float(np.mean(scores)) if scores else 0.0