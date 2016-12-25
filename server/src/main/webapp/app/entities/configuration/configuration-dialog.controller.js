(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('ConfigurationDialogController', ConfigurationDialogController);

    ConfigurationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Configuration'];

    function ConfigurationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Configuration) {
        var vm = this;

        vm.configuration = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.configuration.id !== null) {
                Configuration.update(vm.configuration, onSaveSuccess, onSaveError);
            } else {
                Configuration.save(vm.configuration, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('yuPosApp:configurationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
