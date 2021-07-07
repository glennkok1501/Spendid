package sg.edu.np.spendid;

//class to match name of category to icon
public class CategoryHandler {

    public CategoryHandler() {
    }

    public int setIcon(String cat){
        int img;
        switch (cat) {
            case "Shopping":
                img = R.drawable.ic_shopping_24;
                break;
            case "Food & Drinks":
                img = R.drawable.ic_food_24;
                break;
            case "Entertainment":
                img = R.drawable.ic_entertainment_24;
                break;
            case "Leisure":
                img = R.drawable.ic_leisure_24;
                break;
            case "Transport":
                img = R.drawable.ic_transport_24;
                break;
            case "Housing":
                img = R.drawable.ic_housing_24;
                break;
            case "Vehicle":
                img = R.drawable.ic_vehicle_24;
                break;
            case "Income":
                img = R.drawable.ic_income_24;
                break;
            case "Salary":
                img = R.drawable.ic_salary_24;
                break;
            case "Bills":
                img = R.drawable.ic_bills_24;
                break;
            case "Rental":
                img = R.drawable.ic_rental_24;
                break;
            case "Education":
                img = R.drawable.ic_education_24;
                break;
            case "Health Care":
                img = R.drawable.ic_healthcare_24;
                break;
            case "Fitness":
                img = R.drawable.ic_fitness_24;
                break;
            case "Debt":
                img = R.drawable.ic_debt_24;
                break;
            case "Stocks":
                img = R.drawable.ic_stocks_24;
                break;
            case "Others":
                img = R.drawable.ic_others_24;
                break;
            default:
                img = R.drawable.ic_category_24;
        }
        return img;
    }
}
