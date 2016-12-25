(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('SkuDialogController', SkuDialogController);

    SkuDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Sku', 'Element'];

    function SkuDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Sku, Element) {
        var vm = this;

        vm.sku = entity;
        vm.clear = clear;
        vm.save = save;
        vm.types = Element.query({filter: 'sku-is-null'});
        $q.all([vm.sku.$promise, vm.types.$promise]).then(function() {
            if (!vm.sku.typeId) {
                return $q.reject();
            }
            return Element.get({id : vm.sku.typeId}).$promise;
        }).then(function(type) {
            vm.types.push(type);
        });

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.sku.id !== null) {
                Sku.update(vm.sku, onSaveSuccess, onSaveError);
            } else {
                Sku.save(vm.sku, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yuPosApp:skuUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
