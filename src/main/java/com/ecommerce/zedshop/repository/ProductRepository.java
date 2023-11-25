package com.ecommerce.zedshop.repository;

import com.ecommerce.zedshop.model.Product;
import com.ecommerce.zedshop.model.dto.ProductCountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    @Query(value = "select p from Product p inner join Category c on c.id = p.category.id where c.id = ?1 and p.is_deleted = false and p.is_activated = true")
    List<Product> getProductsInCategory(Long categoryId);
    @Query("select p from Product p where p.name like %?1% or p.description like %?1%")
    List<Product> searchProducts(String keyword);
//    @Query("select new com.ecommerce.zedshop.model.dto.ProductCountDTO (p.name,count(o.product.id))from Product p inner join OrderDetail o on p.id=o.product.id where o.orderDate >= current_date and o.orderDate < current_date + 1 group by p.name")
//    List<ProductCountDTO>fastMovingProductsDaily();
//
//    @Query("select new com.ecommerce.zedshop.model.dto.ProductCountDTO (p.name,count(o.product.id))from Product p inner join OrderDetail o on p.id=o.product.id where o.orderDate >= current_date and o.orderDate < current_date + 7 group by p.name")
//    List<ProductCountDTO>fastMovingProductsWeekly();

    @Query("SELECT new com.ecommerce.zedshop.model.dto.ProductCountDTO(p.name, COUNT(o.product.id)) " +
            "FROM Product p " +
            "INNER JOIN OrderDetail o ON p.id = o.product.id " +
            "WHERE o.orderDate >= :startDate AND o.orderDate < :endDate " +
            "GROUP BY p.name")
    List<ProductCountDTO> fastMovingProductsDaily(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT new com.ecommerce.zedshop.model.dto.ProductCountDTO(p.name, COUNT(o.product.id)) " +
            "FROM Product p " +
            "INNER JOIN OrderDetail o ON p.id = o.product.id " +
            "WHERE o.orderDate >= :startDate AND o.orderDate < :endDate " +
            "GROUP BY p.name")
    List<ProductCountDTO> fastMovingProductsWeekly(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT new com.ecommerce.zedshop.model.dto.ProductCountDTO(p.name, COUNT(o.product.id)) " +
            "FROM Product p " +
            "INNER JOIN OrderDetail o ON p.id = o.product.id " +
            "WHERE o.orderDate >= :startDate AND o.orderDate < :endDate " +
            "GROUP BY p.name")
    List<ProductCountDTO> fastMovingProductsMonthly(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT new com.ecommerce.zedshop.model.dto.ProductCountDTO(p.name, COUNT(o.product.id)) " +
            "FROM Product p " +
            "INNER JOIN OrderDetail o ON p.id = o.product.id " +
            "WHERE o.orderDate >= :startDate AND o.orderDate < :endDate " +
            "GROUP BY p.name")
    List<ProductCountDTO> fastMovingProductsAnnually(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

}
