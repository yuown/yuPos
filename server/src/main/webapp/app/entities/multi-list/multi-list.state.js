(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('multi-list', {
            parent: 'entity',
            url: '/multi-list',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MultiLists'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/multi-list/multi-lists.html',
                    controller: 'MultiListController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('multi-list-detail', {
            parent: 'entity',
            url: '/multi-list/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'MultiList'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/multi-list/multi-list-detail.html',
                    controller: 'MultiListDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'MultiList', function($stateParams, MultiList) {
                    return MultiList.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'multi-list',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('multi-list-detail.edit', {
            parent: 'multi-list-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/multi-list/multi-list-dialog.html',
                    controller: 'MultiListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MultiList', function(MultiList) {
                            return MultiList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('multi-list.new', {
            parent: 'multi-list',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/multi-list/multi-list-dialog.html',
                    controller: 'MultiListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                enabled: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('multi-list', null, { reload: 'multi-list' });
                }, function() {
                    $state.go('multi-list');
                });
            }]
        })
        .state('multi-list.edit', {
            parent: 'multi-list',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/multi-list/multi-list-dialog.html',
                    controller: 'MultiListDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['MultiList', function(MultiList) {
                            return MultiList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('multi-list', null, { reload: 'multi-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('multi-list.delete', {
            parent: 'multi-list',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/multi-list/multi-list-delete-dialog.html',
                    controller: 'MultiListDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['MultiList', function(MultiList) {
                            return MultiList.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('multi-list', null, { reload: 'multi-list' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
