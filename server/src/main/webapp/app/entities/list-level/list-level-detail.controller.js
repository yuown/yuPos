(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('ListLevelDetailController', ListLevelDetailController);

    ListLevelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ListLevel', 'MultiList', 'Level'];

    function ListLevelDetailController($scope, $rootScope, $stateParams, previousState, entity, ListLevel, MultiList, Level) {
        var vm = this;

        vm.listLevel = entity;
        vm.previousState = previousState.name;
        vm.multilists = MultiList.query();
        vm.levels = Level.query();

        var unsubscribe = $rootScope.$on('yuPosApp:listLevelUpdate', function(event, result) {
            vm.listLevel = result;
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
