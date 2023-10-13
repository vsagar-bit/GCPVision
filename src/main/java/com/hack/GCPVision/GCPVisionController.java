package com.hack.GCPVision;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GCPVisionController {

    @GetMapping("getImageName")
    public String getImageName() {
        String result = "";
        try {
            // Load the credentials file to authenticate with the Vision API
            GoogleCredentials credentials = ServiceAccountCredentials.fromStream(
                    new FileInputStream("path to your cerds file cloudvisioinapi-creds.json")
            );

            // Create an image byte stream
            ByteString imageBytes = ByteString.readFrom(new FileInputStream("image file path R.png"));

            // Set up the image for processing
            Image image = Image.newBuilder()
                    .setContent(imageBytes)
                    .build();

            // Set up the Vision API client
            try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
                List<AnnotateImageRequest> requests = new ArrayList<>();

                // Add the image to the request
                AnnotateImageRequest request =
                        AnnotateImageRequest.newBuilder()
                                .setImage(image)
                                .addFeatures(com.google.cloud.vision.v1.Feature.newBuilder().setType(com.google.cloud.vision.v1.Feature.Type.LABEL_DETECTION))
                                .build();

                requests.add(request);

                // Perform the request and get the response
                BatchAnnotateImagesResponse responses = vision.batchAnnotateImages(requests);

                for (AnnotateImageResponse res : responses.getResponsesList()) {
                    if (res.hasError()) {
                        System.err.println("Error: " + res.getError().getMessage());
                        return "";
                    }

                    // Process the results
                    System.out.println("Labels:");
                    for (com.google.cloud.vision.v1.EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                        System.out.println(annotation.getDescription() + " (score: " + annotation.getScore() + ")");
                        result += annotation.getDescription();
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading image or credentials: " + e.getMessage());
        }
        return result;
    }
}
