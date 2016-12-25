(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .controller('ListLevelController', ListLevelController);

    ListLevelController.$inject = ['$scope', '$state', 'ListLevel', 'ParseLinks', 'AlertService', 'paginationConstants', 'pagingParams', 'MultiList', 'Level'];

    function ListLevelController ($scope, $state, ListLevel, ParseLinks, AlertService, paginationConstants, pagingParams, MultiList, Level) {
        var vm = this;

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;
        vm.multilists = MultiList.query();
        vm.levels = Level.query();

        loadAll();

        function loadAll () {
            ListLevel.query({
                page: pagingParams.page - 1,
                size: vm.itemsPerPage,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.listLevels = data;
                vm.page = pagingParams.page;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }

        function loadPage(page) {
            vm.page = page;
            vm.transition();
        }

        function transition() {
            $state.transitionTo($state.$current, {
                page: vm.page,
                sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
        
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
