(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('ElementDialogController', ElementDialogController);

    ElementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Element', 'LevelElement'];

    function ElementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Element, LevelElement) {
        var vm = this;

        vm.element = entity;
        vm.clear = clear;
        vm.save = save;
        vm.levelelements = LevelElement.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.element.id !== null) {
                Element.update(vm.element, onSaveSuccess, onSaveError);
            } else {
                Element.save(vm.element, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yuPosApp:elementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
