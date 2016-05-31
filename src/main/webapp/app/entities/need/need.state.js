(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('need', {
            parent: 'entity',
            url: '/need?page&sort&search',
            data: {
                authorities: ['ROLE_APPUSER'],
                pageTitle: 'volunteercrowdApp.need.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/need/needs.html',
                    controller: 'NeedController',
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
                    $translatePartialLoader.addPart('need');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('need-detail', {
            parent: 'entity',
            url: '/need/{id}',
            data: {
                authorities: ['ROLE_APPUSER'],
                pageTitle: 'volunteercrowdApp.need.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/need/need-detail.html',
                    controller: 'NeedDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('need');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Need', function($stateParams, Need) {
                    return Need.get({id : $stateParams.id});
                }]
            }
        })
        .state('need.new', {
            parent: 'need',
            url: '/new',
            data: {
                authorities: ['ROLE_APPUSER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/need/need-dialog.html',
                    controller: 'NeedDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                title: null,
                                description: null,
                                category: null,
                                location: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('need', null, { reload: true });
                }, function() {
                    $state.go('need');
                });
            }]
        })
        .state('need.edit', {
            parent: 'need',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_APPUSER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/need/need-dialog.html',
                    controller: 'NeedDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Need', function(Need) {
                            return Need.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('need', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('need.delete', {
            parent: 'need',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_APPUSER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/need/need-delete-dialog.html',
                    controller: 'NeedDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Need', function(Need) {
                            return Need.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('need', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
