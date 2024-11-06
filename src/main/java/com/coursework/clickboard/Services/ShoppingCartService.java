package com.coursework.clickboard.Services;

import com.coursework.clickboard.Models.DTO.ShopCart.CartItemViewDTO;
import com.coursework.clickboard.Models.DTO.ApiResponse;
import com.coursework.clickboard.Models.Database.Product.Product;
import com.coursework.clickboard.Models.Database.ShoppingCart.CartItem;
import com.coursework.clickboard.Models.Database.ShoppingCart.ShoppingCart;
import com.coursework.clickboard.Models.Database.User.User;
import com.coursework.clickboard.Repositories.CartItemRepository;
import com.coursework.clickboard.Repositories.ShoppingCartRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ShoppingCartService {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private ProductService productService;
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    public void clear(int id){
        return;
    }

    public ApiResponse addToCart(CartItemViewDTO cartItemViewDTO) throws Exception {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(userDetails.getUsername());
        ShoppingCart shoppCart = user.getShoppingCart();
        Product product = productService.getById(cartItemViewDTO.getProduct().getId());
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setCart(shoppCart);
        cartItem.setQuantity(cartItemViewDTO.getQuantity());
        ShoppingCart cart = shoppingCartRepository.findById(cartItem.getCart().getId()).orElseThrow();
        cart.addToCartItems(cartItem);
        cartItemRepository.save(cartItem);
        shoppingCartRepository.save(cart);
        return new ApiResponse(true, "Товар добавлен в корзину"){};
    }

    public ApiResponse removeFromCart(int productId) throws Exception {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(userDetails.getUsername());
        ShoppingCart shoppCart = user.getShoppingCart();

        int cartId = shoppCart.getId();

        Optional<ShoppingCart> optCart = shoppingCartRepository.findById(cartId);
        ShoppingCart cart = optCart.orElseThrow(() -> new BadCredentialsException("Ошибка удаления из корзины"));
        List<CartItem> cartItems = cart.getCartItems();
        CartItem cartItem = cartItems.stream().filter(item -> item.getProduct().getId() == productId).findFirst().orElseThrow(() -> new BadCredentialsException("Товар не найнден в корзине"));
        cart.removeFromCartItems(cartItem);
        cartItemRepository.delete(cartItem);
        shoppingCartRepository.save(cart);
        return new ApiResponse(true, "Успешно"){};
    }

    public ApiResponse reduceProductQuantity(int productId) throws Exception {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(userDetails.getUsername());
        ShoppingCart shoppCart = user.getShoppingCart();
        ShoppingCart cart = shoppingCartRepository.findById(shoppCart.getId()).orElseThrow(() -> new BadCredentialsException("Не удалось изменить количество"));
        int quantity = cart.reduceProductQuantity(productId);
        if (quantity == 0){
            return removeFromCart(productId);
        }
        shoppingCartRepository.save(cart);
        return new ApiResponse(true, "Успешно"){};
    }

    public ApiResponse increaseProductQuantity(int productId) throws Exception {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userService.getByUsername(userDetails.getUsername());
        ShoppingCart shoppCart = user.getShoppingCart();
        ShoppingCart cart = shoppingCartRepository.findById(shoppCart.getId()).orElseThrow(() -> new BadCredentialsException("Не удалось изменить количество"));
        cart.increseProductQuantity(productId);
        shoppingCartRepository.save(cart);
        return new ApiResponse(true, "Успешно"){};
    }
}

