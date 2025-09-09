package com.loopers.domain.commerceevent;

import java.util.List;

public interface CommerceEventRepository {
    CommerceEvent save(CommerceEvent commerceEvent);

    List<CommerceEvent> findPendingEvents();
}
