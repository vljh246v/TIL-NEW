from abc import ABC, abstractmethod
<<<<<<< HEAD
from data_loader import DataLoader
from metric_calculator import MetricCalculator
from models import Dataset, RecommendResult
=======
from utils.data_loader import DataLoader
from utils.metric_calculator import MetricCalculator
from utils.models import Dataset, RecommendResult
>>>>>>> 069334b (commit 처리)


class BaseRecommender(ABC):
    @abstractmethod
    def recommend(self, dataset: Dataset, **kwargs) -> RecommendResult:
        pass

    def run_sample(self) -> None:
        # Movielens 데이터 취득
<<<<<<< HEAD
        movielens = DataLoader(num_users=1000, num_test_items=5, data_path="../recommend/ml-10M100K/").load()
=======
        movielens = DataLoader(num_users=1000, num_test_items=5, data_path="ml-10M100K/").load()
>>>>>>> 069334b (commit 처리)
        # 추천 계산
        recommend_result = self.recommend(movielens)
        # 추천 결과 평가
        metrics = MetricCalculator().calc(
            movielens.test.rating.tolist(),
            recommend_result.rating.tolist(),
            movielens.test_user2items,
            recommend_result.user2items,
            k=10,
        )
        print(metrics)
