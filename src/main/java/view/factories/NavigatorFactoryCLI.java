package view.factories;

import view.Navigator;
import view.NavigatorCLI;

public class NavigatorFactoryCLI extends NavigatorFactory {
    @Override
    public Navigator getNavigator() {
        return new NavigatorCLI();
    }
}
