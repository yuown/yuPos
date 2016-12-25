(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('ElementDetailController', ElementDetailController);

    ElementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Element', 'LevelElement'];

    function ElementDetailController($scope, $rootScope, $stateParams, previousState, entity, Element, LevelElement) {
        var vm = this;

        vm.element = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yuPosApp:elementUpdate', function(event, result) {
            vm.element = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
