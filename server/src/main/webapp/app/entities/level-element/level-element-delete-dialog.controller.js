(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('LevelElementDeleteController',LevelElementDeleteController);

    LevelElementDeleteController.$inject = ['$uibModalInstance', 'entity', 'LevelElement'];

    function LevelElementDeleteController($uibModalInstance, entity, LevelElement) {
        var vm = this;

        vm.levelElement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            LevelElement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
