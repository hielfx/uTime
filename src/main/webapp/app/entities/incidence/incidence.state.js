(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('incidence', {
            parent: 'entity',
            url: '/incidence?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.incidence.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/incidence/incidences.html',
                    controller: 'IncidenceController',
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
                    $translatePartialLoader.addPart('incidence');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('incidence-detail', {
            parent: 'entity',
            url: '/incidence/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.incidence.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/incidence/incidence-detail.html',
                    controller: 'IncidenceDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('incidence');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Incidence', function($stateParams, Incidence) {
                    return Incidence.get({id : $stateParams.id});
                }]
            }
        })
        .state('incidence.new', {
            parent: 'incidence',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incidence/incidence-dialog.html',
                    controller: 'IncidenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                creationDate: null,
                                closed: false,
                                description: null,
                                adminComment: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('incidence', null, { reload: true });
                }, function() {
                    $state.go('incidence');
                });
            }]
        })
        .state('incidence.edit', {
            parent: 'incidence',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incidence/incidence-dialog.html',
                    controller: 'IncidenceDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Incidence', function(Incidence) {
                            return Incidence.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('incidence', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('incidence.delete', {
            parent: 'incidence',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/incidence/incidence-delete-dialog.html',
                    controller: 'IncidenceDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Incidence', function(Incidence) {
                            return Incidence.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('incidence', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
