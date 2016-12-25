(function() {
    'use strict';

    angular
        .module('yuPosApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('element', {
            parent: 'entity',
            url: '/element',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Elements'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/element/elements.html',
                    controller: 'ElementController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('element-detail', {
            parent: 'entity',
            url: '/element/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'Element'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/element/element-detail.html',
                    controller: 'ElementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'Element', function($stateParams, Element) {
                    return Element.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'element',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('element-detail.edit', {
            parent: 'element-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/element/element-dialog.html',
                    controller: 'ElementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Element', function(Element) {
                            return Element.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('element.new', {
            parent: 'element',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/element/element-dialog.html',
                    controller: 'ElementDialogController',
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
                    $state.go('element', null, { reload: 'element' });
                }, function() {
                    $state.go('element');
                });
            }]
        })
        .state('element.edit', {
            parent: 'element',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/element/element-dialog.html',
                    controller: 'ElementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Element', function(Element) {
                            return Element.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('element', null, { reload: 'element' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('element.delete', {
            parent: 'element',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/element/element-delete-dialog.html',
                    controller: 'ElementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Element', function(Element) {
                            return Element.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('element', null, { reload: 'element' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
