(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('ListLevelDialogController', ListLevelDialogController);

    ListLevelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ListLevel', 'MultiList', 'Level'];

    function ListLevelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ListLevel, MultiList, Level) {
        var vm = this;

        vm.listLevel = entity;
        vm.clear = clear;
        vm.save = save;
        vm.multilists = MultiList.query();
        vm.levels = Level.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.listLevel.id !== null) {
                ListLevel.update(vm.listLevel, onSaveSuccess, onSaveError);
            } else {
                ListLevel.save(vm.listLevel, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yuPosApp:listLevelUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
