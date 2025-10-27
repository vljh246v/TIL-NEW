import pandas as pd
import numpy as np
import os
from sklearn.metrics import mean_squared_error
from math import sqrt
from abc import ABC, abstractmethod
from collections import defaultdict
from sklearn.metrics.pairwise import cosine_similarity
from typing import Dict, List

import dataclasses
import warnings
warnings.filterwarnings('ignore')

print("A. 데이터 준비 및 전처리\n" + "-"*30)

@dataclasses.dataclass(frozen=True)
# 추천 시스템의 학습과 평가에 사용하는 데이터셋
class Dataset:
    # 학습용 평갓값 데이터셋
    train: pd.DataFrame
    # 테스트용 평갓값 데이터셋
    test: pd.DataFrame
    # 순위 지표의 테스트 데이터셋. 키는 사용자 ID, 값은 사용자가 높이 평가한 아이템의 ID 리스트
    test_user2items: Dict[int, List[int]]
    # 아이템 콘텐츠 정보
    item_content: pd.DataFrame


@dataclasses.dataclass(frozen=True)
# 추천 시스템 예측 결과
class RecommendResult:
    # 테스트 데이터셋의 예측 평갓값. RMSE 평가
    rating: pd.DataFrame
    # 키는 사용자 ID, 값은 추천 아이템 ID 리스트. 순위 지표 평가.
    user2items: Dict[int, List[int]]


@dataclasses.dataclass(frozen=True)
# 추천 시스템 평가
class Metrics:
    rmse: float
    precision_at_k: float
    recall_at_k: float

    # 평가 결과는 소수 셋째 자리까지만 출력한다
    def __repr__(self):
        return f"rmse={self.rmse:.3f}, Precision@K={self.precision_at_k:.3f}, Recall@K={self.recall_at_k:.3f}"

# 베이스라인 및 협업 필터링 모델을 위한 추상 클래스
# 모든 추천 모델은 이 클래스를 상속받아 recommend 메서드를 구현해야 합니다.
class BaseRecommender(ABC):
    @abstractmethod
    def recommend(self, dataset: Dataset, **kwargs) -> RecommendResult:
        pass

# DataLoader 클래스
# 과제에서 요구하는 데이터셋을 로드하고 분할하는 역할
class DataLoader:
    def __init__(self, num_users: int = 671, num_test_items: int = 5, data_path: str = "../recommend/ml-10M100K/"):
        self.num_users = num_users
        self.num_test_items = num_test_items
        self.data_path = data_path

    def load(self) -> Dataset:
        ratings, movie_content = self._load()
        movielens_train, movielens_test = self._split_data(ratings)
        # ranking 용 평가 데이터는 각 사용자의 평갓값이 4 이상인 영화만을 정답으로 한다
        # 키는 사용자 ID, 값은 사용자가 높이 평가한 아이템의 ID 리스트
        movielens_test_user2items = (
            movielens_test[movielens_test.rating >= 4].groupby("userId").agg({"movieId": list})["movieId"].to_dict()
        )
        return Dataset(movielens_train, movielens_test, movielens_test_user2items, movie_content)

    def _split_data(self, movielens: pd.DataFrame) -> (pd.DataFrame, pd.DataFrame):
        # 학습용과 테스트용으로 데이터를 분할한다
        # 각 사용자의 직전 5개 영화를 평가용으로 사용하고, 그 이외는 학습용으로 한다
        # 먼저, 각 사용자가 평가한 영화의 순서를 계산한다
        # 최근 부여한 영화부터 순서를 부여한다(0부터 시작)
        movielens["rating_order"] = movielens.groupby("userId")["timestamp"].rank(ascending=False, method="first")
        movielens_train = movielens[movielens["rating_order"] > self.num_test_items]
        movielens_test = movielens[movielens["rating_order"] <= self.num_test_items]
        return movielens_train, movielens_test

    def _load(self) -> (pd.DataFrame, pd.DataFrame):
        # MovieLens latest small 데이터셋 로드
        ratings = pd.read_csv(os.path.join(self.data_path, "ratings.csv"))
        movies = pd.read_csv(os.path.join(self.data_path, "movies.csv"))

        # ratings =  pd.read_csv('../recommend/ml-10M100K/ratings.dat',sep='::', engine='python')
        # movies = pd.read_csv('../recommend/ml-10M100K/movies.dat',  sep='::', engine='python')

        # user 수를 num_users로 줄인다 (과제 스펙에 맞게 전체 유저 사용)
        valid_user_ids = sorted(ratings.userId.unique())[:self.num_users]
        ratings = ratings[ratings.userId.isin(valid_user_ids)]

        return ratings, movies



if __name__ == "__main__":
    # 1. 데이터 로드 및 기본 통계 확인
    print("1. 데이터 로드 및 기본 통계 확인")
    data_loader = DataLoader()
    movielens_dataset = data_loader.load()


