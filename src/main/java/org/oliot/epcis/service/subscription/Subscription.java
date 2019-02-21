package org.oliot.epcis.service.subscription;

import java.util.Iterator;

import org.apache.log4j.Level;
import org.bson.BsonDocument;
import org.oliot.epcis.configuration.Configuration;
import org.oliot.epcis.service.query.QueryService;
import org.oliot.model.epcis.SubscriptionType;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;

import com.mongodb.client.MongoCollection;

public class Subscription {
	public static SchedulerFactory schedFact;
	public static Scheduler sched;

	public void init() {

		try {
			schedFact = new org.quartz.impl.StdSchedulerFactory();
			sched = schedFact.getScheduler();
			if (sched.isStarted() == false)
				sched.start();

			MongoCollection<BsonDocument> collection = Configuration.mongoDatabase.getCollection("Subscription",
					BsonDocument.class);

			Iterator<BsonDocument> subIterator = collection.find(BsonDocument.class).iterator();
			QueryService queryService = new QueryService();
			while (subIterator.hasNext()) {
				BsonDocument sub = subIterator.next();
				SubscriptionType subscription = new SubscriptionType(sub);
				if (subscription.getSchedule() != null && subscription.getTrigger() == null) {
					queryService.addScheduleToQuartz(subscription);
				} else if (subscription.getSchedule() == null && subscription.getTrigger() != null) {
					TriggerEngine.addTriggerSubscription(sub.getString("subscriptionID").getValue(), subscription);
				}
			}

		} catch (SchedulerException e) {
			Configuration.logger.log(Level.ERROR, e.toString());
		}
	}
}
