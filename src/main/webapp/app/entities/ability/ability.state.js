(function() {
    'use strict';

    angular
        .module('volunteercrowdApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ability', {
            parent: 'entity',
            url: '/ability?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.ability.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ability/abilities.html',
                    controller: 'AbilityController',
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
                    $translatePartialLoader.addPart('ability');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('ability-detail', {
            parent: 'entity',
            url: '/ability/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'volunteercrowdApp.ability.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ability/ability-detail.html',
                    controller: 'AbilityDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ability');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Ability', function($stateParams, Ability) {
                    return Ability.get({id : $stateParams.id});
                }]
            }
        })
        .state('ability.new', {
            parent: 'ability',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ability/ability-dialog.html',
                    controller: 'AbilityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                hidden: false,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('ability', null, { reload: true });
                }, function() {
                    $state.go('ability');
                });
            }]
        })
        .state('ability.edit', {
            parent: 'ability',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ability/ability-dialog.html',
                    controller: 'AbilityDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ability', function(Ability) {
                            return Ability.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('ability', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ability.delete', {
            parent: 'ability',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ability/ability-delete-dialog.html',
                    controller: 'AbilityDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Ability', function(Ability) {
                            return Ability.get({id : $stateParams.id});
                        }]
                    }
                }).result.then(function() {
                    $state.go('ability', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
