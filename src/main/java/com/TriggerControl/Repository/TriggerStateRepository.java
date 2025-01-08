package com.TriggerControl.Repository;

import com.TriggerControl.Model.TriggerState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TriggerStateRepository extends JpaRepository<TriggerState, String> {
}
