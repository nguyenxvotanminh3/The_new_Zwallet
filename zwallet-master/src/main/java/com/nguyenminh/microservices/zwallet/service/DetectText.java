//package com.nguyenminh.microservices.zwallet.service;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.util.ArrayList;
//
//public class DetectText {
//    public static void detectText() throws IOException {
//        // TODO(developer): Replace these variables before running the sample.
//        String filePath = "path/to/your/image/file.jpg";
//        detectText(filePath);
//    }
//
//    // Detects text in the specified image.
//    public static void detectText(String filePath) throws IOException {
//        List<AnnotateImageRequest> requests = new ArrayList<>();
//
//        ByteString imgBytes = ByteString.readFrom(new FileInputStream(filePath));
//
//        Image img = Image.newBuilder().setContent(imgBytes).build();
//        Feature feat = Feature.newBuilder().setType(Feature.Type.TEXT_DETECTION).build();
//        AnnotateImageRequest request =
//                AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
//        requests.add(request);
//
//        // Initialize client that will be used to send requests. This client only needs to be created
//        // once, and can be reused for multiple requests. After completing all of your requests, call
//        // the "close" method on the client to safely clean up any remaining background resources.
//        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
//            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
//            List<AnnotateImageResponse> responses = response.getResponsesList();
//
//            for (AnnotateImageResponse res : responses) {
//                if (res.hasError()) {
//                    System.out.format("Error: %s%n", res.getError().getMessage());
//                    return;
//                }
//
//                // For full list of available annotations, see http://g.co/cloud/vision/docs
//                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
//                    System.out.format("Text: %s%n", annotation.getDescription());
//                    System.out.format("Position : %s%n", annotation.getBoundingPoly());
//                }
//            }
//        }
//    }
//}