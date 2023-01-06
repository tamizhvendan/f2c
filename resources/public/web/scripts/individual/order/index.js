window.f2c = window.f2c || {};

(function (f2c) {
  f2c.orderItemCreate = function (itemId, quantity, price, formSubmitUrl) {
    return {
      isEditingQuantity: false,
      updateQuantity() {
        this.isEditingQuantity = true
      }
    }
  }
})(window.f2c);