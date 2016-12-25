(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('LevelElementDialogController', LevelElementDialogController);

    LevelElementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'LevelElement', 'Level', 'Element'];

    function LevelElementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, LevelElement, Level, Element) {
        var vm = this;

        vm.levelElement = entity;
        vm.clear = clear;
        vm.save = save;
        vm.levels = Level.query();
        vm.elements = Element.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.levelElement.id !== null) {
                LevelElement.update(vm.levelElement, onSaveSuccess, onSaveError);
            } else {
                LevelElement.save(vm.levelElement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yuPosApp:levelElementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
