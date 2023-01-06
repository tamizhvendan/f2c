window.f2c = window.f2c || {};

(function (f2c) {
  f2c.priceUpdate = function (itemId, price, pricingUnit, humanizedPrice, formSubmitUrl) {
    return {
      isEditingPrice: false,
      isSaving: false,
      price: price,
      hasNoPrice: price === null,
      humanizedPrice: humanizedPrice,
      canShowSetPriceCta() {
        return !this.isEditingPrice && this.hasNoPrice && !this.isSaving
      },
      canDisplayPrice() {
        return !this.isEditingPrice && !this.hasNoPrice && !this.isSaving;
      },
      editPrice($refs, $nextTick) {
        this.isEditingPrice = true
        $nextTick(() => $refs[itemId + "-" + pricingUnit].focus());
      },
      cancel() {
        this.isEditingPrice = false
        this.price = price
      },
      canSave() {
        return !!this.price;
      },
      save() {
        this.isEditingPrice = false
        this.isSaving = true;

        let formData = new FormData();
        formData.append('community.item-price/item-id', itemId);
        formData.append('community.item-price/price', this.price);
        formData.append('community.item-price/pricing-unit', pricingUnit);
        fetch(formSubmitUrl, { method: 'PUT', body: new URLSearchParams(formData) })
          .then(response => {
            this.isSaving = false;
            if (!response.ok) {
              this.isEditingPrice = true
              alert('Sorry, Unable to update price');
              return
            }
            this.hasNoPrice = false;
            response.json().then((data) => {
              this.humanizedPrice = data.humanizedPrice;
            });
          });
      }
    };
  };
})(window.f2c);