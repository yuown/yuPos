(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('ElementDeleteController',ElementDeleteController);

    ElementDeleteController.$inject = ['$uibModalInstance', 'entity', 'Element'];

    function ElementDeleteController($uibModalInstance, entity, Element) {
        var vm = this;

        vm.element = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Element.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
