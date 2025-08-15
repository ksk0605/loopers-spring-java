package com.loopers.domain.like;

import java.util.List;
import java.util.Optional;

public interface LikeSummaryRepository {
    Optional<LikeSummary> findByTarget(LikeTarget target);

    LikeSummary save(LikeSummary likeSummary);

    List<TargetLikeCount> findAllByTargetIn(List<LikeTarget> targets);
}
