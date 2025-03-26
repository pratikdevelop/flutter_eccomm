package com.example.invoiceapp.service;

import com.example.invoiceapp.model.User;
import org.springframework.stereotype.Service;

@Service
public class RecommendationService {

    public Recommendation generateRecommendations(User user) {
        Recommendation rec = new Recommendation();

        switch (user.getCity()) {
            case "New York":
                rec.setContent("Latest Broadway shows");
                rec.setProduct("City tour package");
                break;
            case "San Francisco":
                rec.setContent("Tech documentaries");
                rec.setProduct("Gadgets");
                break;
            default:
                rec.setContent("General news");
                rec.setProduct("Gift card");
        }

        if ("male".equals(user.getGender())) {
            rec.setProduct("Tech gadgets");
        } else if ("female".equals(user.getGender())) {
            rec.setProduct("Fashion accessories");
        }

        if (user.getPreferences() != null && user.getPreferences().containsKey("favorite_category")) {
            rec.setProduct(user.getPreferences().get("favorite_category") + " item");
        }

        return rec;
    }

    public static class Recommendation {
        private String content;
        private String product;

        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getProduct() { return product; }
        public void setProduct(String product) { this.product = product; }
    }
}