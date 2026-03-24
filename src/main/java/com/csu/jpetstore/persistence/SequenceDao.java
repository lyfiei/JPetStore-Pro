package com.csu.jpetstore.persistence;

import com.csu.jpetstore.domain.Sequence;

public interface SequenceDao {

    Sequence getSequence(Sequence sequence);
    void updateSequence(Sequence sequence);
}
