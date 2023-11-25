package com.taxah.jj.dz1.task2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;


/**
 * Корзина
 *
 * @param <T> Еда
 */
public class Cart<T extends Food> {

    /**
     * Товары в магазине
     */
    private final ArrayList<T> foodstuffs;
    private final UMarket market;
    private final Class<T> clazz;

    public Cart(Class<T> clazz, UMarket market) {
        this.clazz = clazz;
        this.market = market;
        foodstuffs = new ArrayList<>();
    }

    public Collection<T> getFoodstuffs() {
        return foodstuffs;
    }

    /**
     * Распечатать список продуктов в корзине
     */
    public void printFoodstuffs() {
        AtomicInteger index = new AtomicInteger(1);
        foodstuffs.forEach(food -> {
            System.out.printf("[%d] %-22s (Б: %3s | Ж: %3s | У: %3s)\n",
                    index.getAndIncrement(), food.getName(),
                    food.getProteins() ? "Да" : "Нет",
                    food.getFats() ? "Да" : "Нет",
                    food.getCarbohydrates() ? "Да" : "Нет");
        });
    }

    /*
        2. *: Переработать метод балансировки корзины товаров cardBalancing() с использованием Stream API
     */

    /**
     * Балансировка корзины
     */
    public void cardBalancing() {
        boolean proteins = foodstuffs.stream().anyMatch(Food::getProteins);
        boolean fats = foodstuffs.stream().anyMatch(Food::getFats);
        boolean carbohydrates = foodstuffs.stream().anyMatch(Food::getCarbohydrates);

        if (proteins && fats && carbohydrates) {
            System.out.println("Корзина уже сбалансирована по БЖУ.");
            return;
        }

        proteins = addIfNotPresent(proteins, Food::getProteins);
        fats = addIfNotPresent(fats, Food::getFats);
        carbohydrates = addIfNotPresent(carbohydrates, Food::getCarbohydrates);

        if (proteins && fats && carbohydrates)
            System.out.println("Корзина сбалансирована по БЖУ.");
        else
            System.out.println("Невозможно сбалансировать корзину по БЖУ.");
    }
    private boolean addIfNotPresent(boolean macronutrient, Predicate<Food> method) {
        if (!macronutrient) {
            market.getThings(Food.class).stream()
                    .filter(method)
                    .findFirst().ifPresent(food -> foodstuffs.add((T) food));
            return foodstuffs.stream().anyMatch(method);
            // необходимо в случае если .ifPresent optional value = null
        }
        return true;
    }
}
