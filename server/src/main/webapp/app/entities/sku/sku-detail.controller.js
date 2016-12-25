(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('SkuDetailController', SkuDetailController);

    SkuDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Sku', 'Element'];

    function SkuDetailController($scope, $rootScope, $stateParams, previousState, entity, Sku, Element) {
        var vm = this;

        vm.sku = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('yuPosApp:skuUpdate', function(event, result) {
            vm.sku = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
