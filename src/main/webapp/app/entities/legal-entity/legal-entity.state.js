(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('legal-entity', {
            parent: 'entity',
            url: '/legal-entity?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.legalEntity.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/legal-entity/legal-entities.html',
                    controller: 'LegalEntityController',
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
                    $translatePartialLoader.addPart('legalEntity');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('legal-entity-detail', {
            parent: 'entity',
            url: '/legal-entity/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.legalEntity.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/legal-entity/legal-entity-detail.html',
                    controller: 'LegalEntityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('legalEntity');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'LegalEntity', function($stateParams, LegalEntity) {
                    return LegalEntity.get({id : $stateParams.id});
                }]
            }
        })
        .state('legal-entity.new', {
            parent: 'legal-entity',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/legal-entity/legal-entity-dialog.html',
                    controller: 'LegalEntityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                mission: null,
                                vision: null,
                                website: null,
                                description: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('legal-entity', null, { reload: true });
                }, function() {
                    $state.go('legal-entity');
                });
            }]
        })
        .state('legal-entity.edit', {
            parent: 'legal-entity',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/legal-entity/legal-entity-dialog.html',
                    controller: 'LegalEntityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['LegalEntity', function(LegalEntity) {
                            return LegalEntity.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('legal-entity', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('legal-entity.delete', {
            parent: 'legal-entity',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/legal-entity/legal-entity-delete-dialog.html',
                    controller: 'LegalEntityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['LegalEntity', function(LegalEntity) {
                            return LegalEntity.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('legal-entity', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
