(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('MultiListDialogController', MultiListDialogController);

    MultiListDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'MultiList', 'ListLevel'];

    function MultiListDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, MultiList, ListLevel) {
        var vm = this;

        vm.multiList = entity;
        vm.clear = clear;
        vm.save = save;
        vm.listlevels = ListLevel.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.multiList.id !== null) {
                MultiList.update(vm.multiList, onSaveSuccess, onSaveError);
            } else {
                MultiList.save(vm.multiList, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yuPosApp:multiListUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
