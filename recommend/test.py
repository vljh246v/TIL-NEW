# analyze_movielens.py

# 1. 라이브러리 불러오기
import pandas as pd
import numpy as np
import os
from typing import List
from sklearn.metrics import mean_squared_error

# RMSE 계산 함수 정의 (self 제거)
def calc_rmse(true_rating: List[float], pred_rating: List[float]) -> float:
    """
    실제 평점과 예측 평점 리스트를 받아 RMSE를 계산하는 함수
    """
    return np.sqrt(mean_squared_error(true_rating, pred_rating))


# --- 메인 스크립트 실행 ---
print("스크립트 실행 시작...")

# 2. 데이터 경로 설정
base_path = '../recommend/ml-10M100K/'
ratings_path = os.path.join(base_path, 'ratings.dat')
movies_path = os.path.join(base_path, 'movies.dat')

# 3. 데이터 불러오기
print(f"'{ratings_path}' 에서 ratings 데이터를 불러옵니다...")
r_cols = ['user_id', 'movie_id', 'rating', 'timestamp']
ratings = pd.read_csv(ratings_path, names=r_cols, sep='::', engine='python')

print(f"'{movies_path}' 에서 movies 데이터를 불러옵니다...")
m_cols = ['movie_id', 'title', 'genre']
movies = pd.read_csv(movies_path, names=m_cols, sep='::', engine='python', encoding='latin-1')

# 4. 데이터 전처리
print("데이터 전처리를 시작합니다...")
movies['genre'] = movies['genre'].apply(lambda x: x.split('|'))
movielens = ratings.merge(movies, on='movie_id')

# 5. 훈련(Train) / 시험(Test) 데이터 분리
print("시간 순서에 따라 훈련/시험 데이터로 분리합니다...")
movielens['timestamp_rank'] = movielens.groupby('user_id')['timestamp'].rank(ascending=False, method='first')
movielens_train = movielens[movielens['timestamp_rank'] > 5]
movielens_test = movielens[movielens['timestamp_rank'] <= 5]

# 6. 예측값 생성 (Baseline 모델)
print("가장 기본적인 모델을 사용해 예측값을 생성합니다...")
# 훈련 데이터의 전체 평점 평균을 계산
global_mean = movielens_train['rating'].mean()
print(f"훈련 데이터의 전체 평균 평점: {global_mean:.4f}")

# 시험 데이터의 모든 예측값을 전체 평균 평점으로 채움
pred_rating = [global_mean] * len(movielens_test)

# 7. RMSE 계산 및 결과 출력
print("RMSE를 계산합니다...")
# 시험 데이터의 실제 평점 리스트
true_rating = movielens_test['rating'].tolist()

# calc_rmse 함수 호출
rmse = calc_rmse(true_rating, pred_rating)

# --- 최종 결과 출력 ---
print("\n--- 작업 완료 ---")
print(f"전체 평점 데이터 수: {len(movielens)}")
print(f"훈련 데이터 수 (Train set): {len(movielens_train)}")
print(f"시험 데이터 수 (Test set): {len(movielens_test)}")
print(f"\nBaseline 모델의 RMSE 결과: {rmse:.4f}")
print("-----------------")