package com.example.base.event;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;

public class SampleS3LambdaEvent implements RequestHandler<S3Event, String> {
    @Override
    public String handleRequest(S3Event s3Event, Context context) {
        for (S3EventNotification.S3EventNotificationRecord record : s3Event.getRecords()) {
            String bucketName = record.getS3().getBucket().getName();
            String objectKey = record.getS3().getObject().getKey();
            System.out.println("Received S3 event from "+ bucketName + "for object " + objectKey);

            // Add your custom logic to handle the S3 event
        }

        return "S3 Event Processed";
    }
}
