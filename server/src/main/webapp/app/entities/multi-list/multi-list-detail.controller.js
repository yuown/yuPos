(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('MultiListDetailController', MultiListDetailController);

    MultiListDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'MultiList', 'ListLevel'];

    function MultiListDetailController($scope, $rootScope, $stateParams, previousState, entity, MultiList, ListLevel) {
        var vm = this;

        vm.multiList = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yuPosApp:multiListUpdate', function(event, result) {
            vm.multiList = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
