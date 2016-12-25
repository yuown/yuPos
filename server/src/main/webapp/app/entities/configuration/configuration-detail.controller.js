(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('ConfigurationDetailController', ConfigurationDetailController);

    ConfigurationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Configuration'];

    function ConfigurationDetailController($scope, $rootScope, $stateParams, previousState, entity, Configuration) {
        var vm = this;

        vm.configuration = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yuPosApp:configurationUpdate', function(event, result) {
            vm.configuration = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
