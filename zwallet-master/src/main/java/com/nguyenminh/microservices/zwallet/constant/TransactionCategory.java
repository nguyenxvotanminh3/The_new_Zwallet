package com.nguyenminh.microservices.zwallet.constant;

public enum TransactionCategory {
    FOOD, BILL, ENTERTAIN, SHOPPING, INVESTMENT, MEDICINE, EDUCATION, TRAVEL, RENT,
    TRANSPORTATION, UTILITIES, SAVINGS, CHARITY, INSURANCE, GIFTS, RECEIVE_MONEY, TRANSFER_MONEY, OTHERS, CHANGE_CURRENT_MONEY;

    public static TransactionCategory fromString(String category) {
        return switch (category.toLowerCase()) {
            case "food" -> FOOD;
            case "bill" -> BILL;
            case "entertain" -> ENTERTAIN;
            case "shopping" -> SHOPPING;
            case "investment" -> INVESTMENT;
            case "medicine" -> MEDICINE;
            case "education" -> EDUCATION;
            case "travel" -> TRAVEL;
            case "rent" -> RENT;
            case "transportation" -> TRANSPORTATION;
            case "utilities" -> UTILITIES;
            case "savings" -> SAVINGS;
            case "charity" -> CHARITY;
            case "insurance" -> INSURANCE;
            case "gifts" -> GIFTS;
            case "receive_money" -> RECEIVE_MONEY;
            case "transfer_money" -> TRANSFER_MONEY;
            case "others" -> OTHERS;
            case "change current money" -> CHANGE_CURRENT_MONEY; // Thêm xử lý danh mục này
            default -> throw new IllegalArgumentException("Unknown category: " + category);
        };

}
}

