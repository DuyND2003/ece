package com.example.ece.CommandLineRunner;

import com.example.ece.entity.Category;
import com.example.ece.entity.Product;
import com.example.ece.repository.ProductRepository;
import net.datafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@Component
public class FakeDataGenerator implements CommandLineRunner {
    private final ProductRepository productRepository;
    private final Faker faker = new Faker();
    private Random random = new Random();

    public FakeDataGenerator(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (productRepository.count() == 0){
            generateFakeData(300);
        }

    }

    private void generateFakeData(int count) {
        List<Category> categories = List.of(Category.ELECTRONICS, Category.BOOKS,
                Category.FURNITURE, Category.CLOTHES, Category.FOODS);

        for (int i = 0; i < count; i++) {
            String productName;
            do {
                productName = faker.commerce().productName() + " " + UUID.randomUUID();
            } while (productRepository.existsByName(productName));

            Product product = Product.builder()
                    .name(productName)
                    .description(faker.lorem().sentence(10))
                    .price(BigDecimal.valueOf(random.nextInt(10000000, 50000000)))
                    .stockQuantity(random.nextInt(5, 50))
                    .imageUrl(faker.internet().image())
                    .category(categories.get(faker.number().numberBetween(0, categories.size())))
                    .build();
            productRepository.save(product);
            System.out.println("✅ Product saved: " + product);
        }
    }

}
