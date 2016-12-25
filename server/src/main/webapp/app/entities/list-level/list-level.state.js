(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('list-level', {
            parent: 'entity',
            url: '/list-level?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ListLevels'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/list-level/list-levels.html',
                    controller: 'ListLevelController',
                    controllerAs: 'vm'
                }
            },
            params: {
                page: {
                    value: '1',
                    squash: true
                },
                sort: {
                    value: 'id,asc',
                    squash: true
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
            }
        })
        .state('list-level-detail', {
            parent: 'entity',
            url: '/list-level/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ListLevel'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/list-level/list-level-detail.html',
                    controller: 'ListLevelDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'ListLevel', function($stateParams, ListLevel) {
                    return ListLevel.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'list-level',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('list-level-detail.edit', {
            parent: 'list-level-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/list-level/list-level-dialog.html',
                    controller: 'ListLevelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ListLevel', function(ListLevel) {
                            return ListLevel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('list-level.new', {
            parent: 'list-level',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/list-level/list-level-dialog.html',
                    controller: 'ListLevelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                rank: null,
                                active: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('list-level', null, { reload: 'list-level' });
                }, function() {
                    $state.go('list-level');
                });
            }]
        })
        .state('list-level.edit', {
            parent: 'list-level',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/list-level/list-level-dialog.html',
                    controller: 'ListLevelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ListLevel', function(ListLevel) {
                            return ListLevel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('list-level', null, { reload: 'list-level' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('list-level.delete', {
            parent: 'list-level',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/list-level/list-level-delete-dialog.html',
                    controller: 'ListLevelDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ListLevel', function(ListLevel) {
                            return ListLevel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('list-level', null, { reload: 'list-level' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
