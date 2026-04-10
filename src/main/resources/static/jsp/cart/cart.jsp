<%@ include file="../common/top.jsp"%>
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{common/top :: head}">
    <meta charset="UTF-8">
    <title>Shopping Cart - MyPetStore</title>
</head>
<body th:fragment="cart">
<div th:replace="~{common/top :: top}"></div>

<div id="BackLink">
    <a th:href="@{/mainForm}">Return to Main Menu</a>
</div>

<div id="Catalog">
    <div id="Cart">
        <h2>Shopping Cart</h2>

        <table>
            <tr>
                <th><b>Item ID</b></th>
                <th><b>Product ID</b></th>
                <th><b>Description</b></th>
                <th><b>In Stock?</b></th>
                <th><b>Quantity</b></th>
                <th><b>List Price</b></th>
                <th><b>Total Cost</b></th>
                <th>&nbsp;</th>
            </tr>

            <tr th:if="${session.cart == null or session.cart.numberOfItems == 0}">
                <td colspan="8"><b>Your cart is empty.</b></td>
            </tr>

            <tr th:each="cartItem : ${session.cart.cartItems}">
                <td>
                    <a th:href="@{/itemForm(itemId=${cartItem.item.itemId})}" th:text="${cartItem.item.itemId}">I1</a>
                </td>

                <td th:text="${cartItem.item.product.productId}">P1</td>
                <td>
                    <span th:text="${cartItem.item.attribute1 + ' ' + cartItem.item.attribute2 + ' ' + cartItem.item.attribute3 + ' ' + cartItem.item.attribute4 + ' ' + cartItem.item.attribute5 + ' ' + cartItem.item.product.name}">Description</span>
                </td>
                <td th:text="${cartItem.inStock}">true</td>
                <td>
                    <input type="number"
                           class="quantity-input"
                           th:data-itemid="${cartItem.item.itemId}"
                           th:value="${cartItem.quantity}"
                           min="1"/>
                </td>

                <td th:text="'$' + ${#numbers.formatDecimal(cartItem.item.listPrice, 1, 'COMMA', 2, 'POINT')}">$0.00</td>
                <td>
                    <span th:id="'total-' + ${cartItem.item.itemId}">
                        <span th:text="'$' + ${#numbers.formatDecimal(cartItem.total, 1, 'COMMA', 2, 'POINT')}">$0.00</span>
                    </span>
                </td>
                <td>
                    <a href="javascript:void(0);"
                       class="Button"
                       th:onclick="'removeItemAjax(\'' + ${cartItem.item.itemId} + '\', this)'">
                        remove
                    </a>
                </td>
            </tr>

            <tr th:if="${session.cart != null and session.cart.numberOfItems > 0}">
                <td colspan="7">
                    Sub Total:
                    <span id="sub-total">
                        <span th:text="'$' + ${#numbers.formatDecimal(session.cart.subTotal, 1, 'COMMA', 2, 'POINT')}">$0.00</span>
                    </span>
                </td>
                <td>&nbsp;&nbsp;</td>
            </tr>
        </table>

        <a th:if="${session.cart != null and session.cart.numberOfItems > 0}"
           th:href="@{/orderUnified}"
           class="Button">Proceed to Checkout</a>
    </div>

    <div id="MyList">
        <th:block th:if="${session.loginAccount != null}">
            <th:block th:if="${!session.loginAccount.authenticated}">
                <th:block th:if="${not #strings.isEmpty(session.loginAccount.listOption)}">
                    <div th:replace="~{cart/includeMyList :: myList}"></div>
                </th:block>
            </th:block>
        </th:block>
    </div>

    <div id="Separator">&nbsp;</div>
</div>

<div th:replace="~{common/bottom :: bottom}"></div>

<script th:src="@{/js/cart.js}"></script>

</body>
</html>
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head th:replace="~{common/top :: head}">
    <meta charset="UTF-8">
    <title>Shopping Cart - MyPetStore</title>
</head>
<body th:fragment="cart">
<div th:replace="~{common/top :: top}"></div>

<div id="BackLink">
    <a th:href="@{/mainForm}">Return to Main Menu</a>
</div>

<div id="Catalog">
    <div id="Cart">
        <h2>Shopping Cart</h2>

        <table>
            <tr>
                <th><b>Item ID</b></th>
                <th><b>Product ID</b></th>
                <th><b>Description</b></th>
                <th><b>In Stock?</b></th>
                <th><b>Quantity</b></th>
                <th><b>List Price</b></th>
                <th><b>Total Cost</b></th>
                <th>&nbsp;</th>
            </tr>

            <tr th:if="${session.cart == null or session.cart.numberOfItems == 0}">
                <td colspan="8"><b>Your cart is empty.</b></td>
            </tr>

            <tr th:each="cartItem : ${session.cart.cartItems}">
                <td>
                    <a th:href="@{/itemForm(itemId=${cartItem.item.itemId})}" th:text="${cartItem.item.itemId}">I1</a>
                </td>

                <td th:text="${cartItem.item.product.productId}">P1</td>
                <td>
                    <span th:text="${cartItem.item.attribute1 + ' ' + cartItem.item.attribute2 + ' ' + cartItem.item.attribute3 + ' ' + cartItem.item.attribute4 + ' ' + cartItem.item.attribute5 + ' ' + cartItem.item.product.name}">Description</span>
                </td>
                <td th:text="${cartItem.inStock}">true</td>
                <td>
                    <input type="number"
                           class="quantity-input"
                           th:data-itemid="${cartItem.item.itemId}"
                           th:value="${cartItem.quantity}"
                           min="1"/>
                </td>

                <td th:text="'$' + ${#numbers.formatDecimal(cartItem.item.listPrice, 1, 'COMMA', 2, 'POINT')}">$0.00</td>
                <td>
                    <span th:id="'total-' + ${cartItem.item.itemId}">
                        <span th:text="'$' + ${#numbers.formatDecimal(cartItem.total, 1, 'COMMA', 2, 'POINT')}">$0.00</span>
                    </span>
                </td>
                <td>
                    <a href="javascript:void(0);"
                       class="Button"
                       th:onclick="'removeItemAjax(\'' + ${cartItem.item.itemId} + '\', this)'">
                        remove
                    </a>
                </td>
            </tr>

            <tr th:if="${session.cart != null and session.cart.numberOfItems > 0}">
                <td colspan="7">
                    Sub Total:
                    <span id="sub-total">
                        <span th:text="'$' + ${#numbers.formatDecimal(session.cart.subTotal, 1, 'COMMA', 2, 'POINT')}">$0.00</span>
                    </span>
                </td>
                <td>&nbsp;&nbsp;</td>
            </tr>
        </table>

        <a th:if="${session.cart != null and session.cart.numberOfItems > 0}"
           th:href="@{/orderUnified}"
           class="Button">Proceed to Checkout</a>
    </div>

    <div id="MyList">
        <th:block th:if="${session.loginAccount != null}">
            <th:block th:if="${!session.loginAccount.authenticated}">
                <th:block th:if="${not #strings.isEmpty(session.loginAccount.listOption)}">
                    <div th:replace="~{cart/includeMyList :: myList}"></div>
                </th:block>
            </th:block>
        </th:block>
    </div>

    <div id="Separator">&nbsp;</div>
</div>

<div th:replace="~{common/bottom :: bottom}"></div>

<script th:src="@{/js/cart.js}"></script>

</body>
</html>

<div id="BackLink">
  <a href="mainForm">Return to Main Menu</a>
</div>

<div id="Catalog">

  <div id="Cart">

    <h2>Shopping Cart</h2>

        <table>
          <tr>
            <th><b>Item ID</b></th>
            <th><b>Product ID</b></th>
            <th><b>Description</b></th>
            <th><b>In Stock?</b></th>
            <th><b>Quantity</b></th>
            <th><b>List Price</b></th>
            <th><b>Total Cost</b></th>
            <th>&nbsp;</th>
          </tr>

          <c:if test="${sessionScope.cart.numberOfItems == 0}">
            <tr>
              <td colspan="8"><b>Your cart is empty.</b></td>
            </tr>
          </c:if>

          <c:forEach var="cartItem" items="${sessionScope.cart.cartItems}">
            <tr>
              <td>
              <a href="itemForm?itemId=${cartItem.item.itemId}"> ${cartItem.item.itemId}</a>
              </td>

              <td>${cartItem.item.product.productId}</td>
              <td>${cartItem.item.attribute1} ${cartItem.item.attribute2}
                  ${cartItem.item.attribute3} ${cartItem.item.attribute4}
                  ${cartItem.item.attribute5} ${cartItem.item.product.name}</td>
              <td>${cartItem.inStock}</td>
              <td>
                <input type = "number"
                        class = "quantity-input"
                        data-itemid = "${cartItem.item.itemId}"
                        value = "${cartItem.quantity}"
                        min = "1">
              </td>

              <td><fmt:formatNumber value="${cartItem.item.listPrice}"
                                    pattern="$#,##0.00" /></td>
              <td>
                <span id="total-${cartItem.item.itemId}">
                  <fmt:formatNumber value="${cartItem.total}" pattern="$#,##0.00" />
                </span>

              </td>
              <td>
                <a href="javascript:void(0);"
                   class="Button"
                   onclick="removeItemAjax('${cartItem.item.itemId}', this)">
                  remove
                </a>
              </td>
            </tr>
          </c:forEach>
          <tr>
            <td colspan="7">
              Sub Total: <span id="sub-total">
        <fmt:formatNumber value="${sessionScope.cart.subTotal}" pattern="$#,##0.00" />
                </span>
            </td>
            <td>&nbsp&nbsp;</td>
          </tr>
        </table>

      <c:if test="${sessionScope.cart.numberOfItems > 0}">

          <a href="orderUnified" class="Button">Proceed to Checkout</a>
      </c:if>



  </div>

  <div id="MyList">
    <c:if test="${sessionScope.loginAccount != null}">
      <c:if test="${!sessionScope.loginAcount.authenticated}">
        <c:if test="${!empty sessionScope.loginAccount.listOption}">
          <%@ include file="includeMyList.jsp"%>
        </c:if>
      </c:if>
    </c:if>
  </div>

  <div id="Separator">&nbsp;</div>
</div>

<%@ include file="../common/bottom.jsp"%>

<script src="${pageContext.request.contextPath}/js/cart.js"></script>
