(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('LevelElementDetailController', LevelElementDetailController);

    LevelElementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'LevelElement', 'Level', 'Element'];

    function LevelElementDetailController($scope, $rootScope, $stateParams, previousState, entity, LevelElement, Level, Element) {
        var vm = this;

        vm.levelElement = entity;
        vm.previousState = previousState.name;
        
        vm.elements = Element.query();
        vm.levels = Level.query();

        var unsubscribe = $rootScope.$on('yuPosApp:levelElementUpdate', function(event, result) {
            vm.levelElement = result;
        });
        $scope.$on('$destroy', unsubscribe);
        
        vm.id2name = function(lst, id) {
            var name = '';
            lst.forEach(function(item) {
                if(item.id == id) {
                    name = item.name;
                    return;
                }
            });
            return name;
        }
    }
})();
