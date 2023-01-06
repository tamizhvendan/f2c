window.f2c = window.f2c || {};

(function (f2c) {
  f2c.orderItemCreate = function (itemId, quantity, unit, formSubmitUrl) {
    return {
      isEditingQuantity: false,
      isSaving: false,
      quantity: quantity,
      unit: unit,
      hasAlreadyAdded() {
        return !!this.quantity && !!this.unit;
      },
      canDisplayQuantity() {
        return !this.isEditingQuantity && this.hasAlreadyAdded() && !this.isSaving;
      },
      cancel() {
        this.isEditingQuantity = false
        this.quantity = quantity
        this.unit = unit
      },
      canSave() {
        return this.quantity > 0.1 && !!this.unit
      },
      canAddQuantity() {
        return !this.isEditingQuantity && !this.hasAlreadyAdded() && !this.isSaving;
      },
      addQuantity($refs, $nextTick) {
        this.isEditingQuantity = true;
        $nextTick(() => $refs[itemId].focus());
      },
      save($event) {
        this.isEditingQuantity = false
        this.isSaving = true;

        let formData = new FormData();
        formData.append('individual.order-item/item-id', itemId);
        formData.append('individual.order-item/quantity', this.quantity);
        formData.append('individual.order-item/unit', this.unit);
        fetch(formSubmitUrl, { method: 'POST', body: new URLSearchParams(formData) })
          .then(response => {
            this.isSaving = false;
            if (!response.ok) {
              this.isEditingQuantity = true
              alert('Sorry, Unable to update order');
              return
            }

          });
      }
    }
  }
})(window.f2c);