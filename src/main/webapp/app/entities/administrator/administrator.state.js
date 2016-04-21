(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('administrator', {
            parent: 'entity',
            url: '/administrator?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.administrator.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/administrator/administrators.html',
                    controller: 'AdministratorController',
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
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('administrator');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('administrator-detail', {
            parent: 'entity',
            url: '/administrator/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.administrator.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/administrator/administrator-detail.html',
                    controller: 'AdministratorDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('administrator');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Administrator', function($stateParams, Administrator) {
                    return Administrator.get({id : $stateParams.id});
                }]
            }
        })
        .state('administrator.new', {
            parent: 'administrator',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/administrator/administrator-dialog.html',
                    controller: 'AdministratorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('administrator', null, { reload: true });
                }, function() {
                    $state.go('administrator');
                });
            }]
        })
        .state('administrator.edit', {
            parent: 'administrator',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/administrator/administrator-dialog.html',
                    controller: 'AdministratorDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Administrator', function(Administrator) {
                            return Administrator.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('administrator', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('administrator.delete', {
            parent: 'administrator',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/administrator/administrator-delete-dialog.html',
                    controller: 'AdministratorDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Administrator', function(Administrator) {
                            return Administrator.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('administrator', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
