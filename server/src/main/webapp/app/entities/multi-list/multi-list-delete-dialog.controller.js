(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('MultiListDeleteController',MultiListDeleteController);

    MultiListDeleteController.$inject = ['$uibModalInstance', 'entity', 'MultiList'];

    function MultiListDeleteController($uibModalInstance, entity, MultiList) {
        var vm = this;

        vm.multiList = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            MultiList.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
