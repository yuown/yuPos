(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('LevelDialogController', LevelDialogController);

    LevelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Level', 'ListLevel', 'LevelElement'];

    function LevelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Level, ListLevel, LevelElement) {
        var vm = this;

        vm.level = entity;
        vm.clear = clear;
        vm.save = save;
        vm.listlevels = ListLevel.query();
        vm.levelelements = LevelElement.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.level.id !== null) {
                Level.update(vm.level, onSaveSuccess, onSaveError);
            } else {
                Level.save(vm.level, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yuPosApp:levelUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
