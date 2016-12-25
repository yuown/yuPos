(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('ListLevelDeleteController',ListLevelDeleteController);

    ListLevelDeleteController.$inject = ['$uibModalInstance', 'entity', 'ListLevel'];

    function ListLevelDeleteController($uibModalInstance, entity, ListLevel) {
        var vm = this;

        vm.listLevel = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ListLevel.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
