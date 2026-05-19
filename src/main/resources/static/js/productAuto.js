$(function (){
    $('#keyword').on('keyup',function () {
        var keyword = $(this).val();
        // console.log(keyword);
        // if(keyword !== ''&& keyword!==null && keyword.length !== 0){
        if (keyword && keyword.trim().length > 0) {
            $.ajax({
                type   : 'GET',
                // url    : 'http://localhost:8080/mypetstore_war_exploded/productAuto?keyword='+keyword,
                url    : '/catalog/autoComplete',
                data: { keyword: keyword },
                success: function(data){
                    // console.log(data);
                    var productListHTML = '';
                    // for(var i = 0; i < data.length; i++){
                    //     productListHTML += '<li class=\"productAutoItem\" data-productId="';
                    //     productListHTML += data[i].productId;
                    //     productListHTML +='">';
                    //     productListHTML += data[i].categoryId;
                    //     productListHTML += ': ';
                    //     productListHTML += data[i].name;
                    //     productListHTML +='</li>';
                    // }
                    // $('#productAutoList').html(productListHTML)
                    // $('#productAutoComplete').show();
                    for (var i = 0; i < data.length; i++) {
                        // 优化点 3: 使用模板字符串 (ES6) 让代码更易读
                        productListHTML += `
                            <li class="productAutoItem" data-productId="${data[i].productId}">
                                ${data[i].categoryId}: ${data[i].name}
                            </li>`;
                    }

                    if (data.length > 0) {
                        $('#productAutoList').html(productListHTML);
                        $('#productAutoComplete').show();
                    } else {
                        $('#productAutoComplete').hide();
                    }
                },
                error  :function (errorMsg){
                    console.log(errorMsg);
                }
            });
        }else{
            $('#productAutoComplete').hide();

        }
    });
    // $('.productAutoItem').on('click', function () {
    //
    // })

    // 动态绑定
    $(document).on('click', '.productAutoItem', function () {
        var productId = $(this).data('productid');
        $('#productAutoComplete').hide();
        $('#keyword').val('');
        // console.log(productId);
        // window.location.href = 'http://localhost:8080/mypetstore_war_exploded/productForm?productId=' + productId;
        window.location.href = 'catalog/product?productId=' + productId;
    });
    
    // 鼠标移开
    // 区分mouseleave与mouseout
    $('#productAutoComplete').on('mouseleave', function () {
        $(this).hide();
        // $('#keyword').val('');
    })
});