(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('LevelDetailController', LevelDetailController);

    LevelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Level', 'ListLevel', 'LevelElement'];

    function LevelDetailController($scope, $rootScope, $stateParams, previousState, entity, Level, ListLevel, LevelElement) {
        var vm = this;

        vm.level = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yuPosApp:levelUpdate', function(event, result) {
            vm.level = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
