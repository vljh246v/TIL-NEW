# from utils.models import RecommendResult, Dataset
# from base_recommender import BaseRecommender
# from collections import defaultdict
# import numpy as np
#
# np.random.seed(0)
#
#
# class RandomRecommender(BaseRecommender):
#     def recommend(self, dataset: Dataset, **kwargs) -> RecommendResult:
#         # 사용자 ID와 아이템 ID에 대해 0부터 시작하는 인덱스를 할당한다
#         unique_user_ids = sorted(dataset.train.user_id.unique())
#         unique_movie_ids = sorted(dataset.train.movie_id.unique())
#         user_id2index = dict(zip(unique_user_ids, range(len(unique_user_ids))))
#         movie_id2index = dict(zip(unique_movie_ids, range(len(unique_movie_ids))))
#
#         # 사용자 x 아이템의 행렬에서 각 셀의 예측 평갓값은 0.5~5.0의 균등 난수로 한다
#         pred_matrix = np.random.uniform(0.5, 5.0, (len(unique_user_ids), len(unique_movie_ids)))
#
#         # rmse 평가용으로 테스트 데이터에 나오는 사용자와 아이템의 예측 평갓값을 저장한다
#         movie_rating_predict = dataset.test.copy()
#         pred_results = []
#         for i, row in dataset.test.iterrows():
#             user_id = row["user_id"]
#             # 테스트 데이터의 아이템 ID가 학습용으로 등장하지 않는 경우도 난수를 저장한다
#             if row["movie_id"] not in movie_id2index:
#                 pred_results.append(np.random.uniform(0.5, 5.0))
#                 continue
#             # 테스트 데이터에 나타나는 사용자 ID와 아이템 ID의 인덱스를 얻어, 평갓값 행렬값을 얻는다
#             user_index = user_id2index[row["user_id"]]
#             movie_index = movie_id2index[row["movie_id"]]
#             pred_score = pred_matrix[user_index, movie_index]
#             pred_results.append(pred_score)
#         movie_rating_predict["rating_pred"] = pred_results
#
#         # 순위 평가용 데이터 작성
#         # 각 사용자에 대한 추천 영화는, 해당 사용자가 아직 평가하지 않은 영화 중에서 무작위로 10개 작품으로 한다
#         # 키는 사용자 ID, 값은 추천 아이템의 ID 리스트
#         pred_user2items = defaultdict(list)
#         # 사용자가 이미 평가한 영화를 저장한다
#         user_evaluated_movies = dataset.train.groupby("user_id").agg({"movie_id": list})["movie_id"].to_dict()
#         for user_id in unique_user_ids:
#             user_index = user_id2index[user_id]
#             movie_indexes = np.argsort(-pred_matrix[user_index, :])
#             for movie_index in movie_indexes:
#                 movie_id = unique_movie_ids[movie_index]
#                 if movie_id not in user_evaluated_movies[user_id]:
#                     pred_user2items[user_id].append(movie_id)
#                 if len(pred_user2items[user_id]) == 10:
#                     break
#         return RecommendResult(movie_rating_predict.rating_pred, pred_user2items)
#
#
#
# class PopularityRecommender(BaseRecommender):
#     def recommend(self, dataset: Dataset, **kwargs) -> RecommendResult:
#         # 평갓값의 임곗값
#         minimum_num_rating = kwargs.get("minimum_num_rating", 200)
#
#         # 각 아이템별 평균 평갓값을 계산하고, 그 평균 평갓값을 예측값으로 사용한다
#         movie_rating_average = dataset.train.groupby("movie_id").agg({"rating": "mean"})
#         # 테스트 데이터에 예측값을 저장한다. 테스트 데이터에만 존재하는 아이템의 예측 평갓값은 0으로 한다
#         movie_rating_predict = dataset.test.merge(
#             movie_rating_average, on="movie_id", how="left", suffixes=("_test", "_pred")
#         ).fillna(0)
#
#         # 각 사용자에 대한 추천 영화는 해당 사용자가 아직 평가하지 않은 영화 중에서 평균값이 높은 10개 작품으로 한다
#         # 단, 평가 건수가 적으면 노이즈가 커지므로 minimum_num_rating건 이상 평가가 있는 영화로 한정한다
#         pred_user2items = defaultdict(list)
#         user_watched_movies = dataset.train.groupby("user_id").agg({"movie_id": list})["movie_id"].to_dict()
#         movie_stats = dataset.train.groupby("movie_id").agg({"rating": ["size", "mean"]})
#         atleast_flg = movie_stats["rating"]["size"] >= minimum_num_rating
#         movies_sorted_by_rating = (
#             movie_stats[atleast_flg].sort_values(by=("rating", "mean"), ascending=False).index.tolist()
#         )
#
#         for user_id in dataset.train.user_id.unique():
#             for movie_id in movies_sorted_by_rating:
#                 if movie_id not in user_watched_movies[user_id]:
#                     pred_user2items[user_id].append(movie_id)
#                 if len(pred_user2items[user_id]) == 10:
#                     break
#
#         return RecommendResult(movie_rating_predict.rating_pred, pred_user2items)
#
#
# if __name__ == "__main__":
#     RandomRecommender().run_sample()
#     PopularityRecommender().run_sample()


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


