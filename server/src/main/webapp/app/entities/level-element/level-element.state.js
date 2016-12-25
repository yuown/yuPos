(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('level-element', {
            parent: 'entity',
            url: '/level-element?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'LevelElements'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/level-element/level-elements.html',
                    controller: 'LevelElementController',
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
        .state('level-element-detail', {
            parent: 'entity',
            url: '/level-element/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'LevelElement'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/level-element/level-element-detail.html',
                    controller: 'LevelElementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'LevelElement', function($stateParams, LevelElement) {
                    return LevelElement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'level-element',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('level-element-detail.edit', {
            parent: 'level-element-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/level-element/level-element-dialog.html',
                    controller: 'LevelElementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LevelElement', function(LevelElement) {
                            return LevelElement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('level-element.new', {
            parent: 'level-element',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/level-element/level-element-dialog.html',
                    controller: 'LevelElementDialogController',
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
                    $state.go('level-element', null, { reload: 'level-element' });
                }, function() {
                    $state.go('level-element');
                });
            }]
        })
        .state('level-element.edit', {
            parent: 'level-element',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/level-element/level-element-dialog.html',
                    controller: 'LevelElementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LevelElement', function(LevelElement) {
                            return LevelElement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('level-element', null, { reload: 'level-element' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('level-element.delete', {
            parent: 'level-element',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/level-element/level-element-delete-dialog.html',
                    controller: 'LevelElementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LevelElement', function(LevelElement) {
                            return LevelElement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('level-element', null, { reload: 'level-element' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
