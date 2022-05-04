package kuesuchan.jpns.database.dao.helper;

import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;

public interface DaoHelper {

    final Scheduler DEFAULT_SCHEDULER = Schedulers.io();

    long insert(Object object);

    int delete(Object object);

    int update(Object object);
}
