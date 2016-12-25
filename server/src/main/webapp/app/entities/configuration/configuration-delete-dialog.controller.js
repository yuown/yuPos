(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('ConfigurationDeleteController',ConfigurationDeleteController);

    ConfigurationDeleteController.$inject = ['$uibModalInstance', 'entity', 'Configuration'];

    function ConfigurationDeleteController($uibModalInstance, entity, Configuration) {
        var vm = this;

        vm.configuration = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Configuration.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
