package com.redhat.ceylon.common.test;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.redhat.ceylon.common.config.CeylonConfig;
import com.redhat.ceylon.common.config.ConfigParser;
import com.redhat.ceylon.common.config.Credentials;
import com.redhat.ceylon.common.config.Repositories;
import com.redhat.ceylon.common.config.Repositories.Repository;

public class RepositoriesTest {

    CeylonConfig testConfig;
    Repositories repos;
    Repositories defaultRepos;
    Repositories overriddenRepos;
    
    @Before
    public void setup() throws IOException {
        testConfig = ConfigParser.loadConfigFromFile(new File("test/src/com/redhat/ceylon/common/test/repos.config"));
        if (CeylonConfig.getInstallDir() == null) {
            // Set a fake installation folder
            System.setProperty("ceylon.home", "fake-install-dir");
        }
        repos = Repositories.withConfig(testConfig);
        
        CeylonConfig fakeConfig = new CeylonConfig();
        defaultRepos = Repositories.withConfig(fakeConfig);
        
        CeylonConfig overriddenConfig = ConfigParser.loadConfigFromFile(new File("test/src/com/redhat/ceylon/common/test/overridden.config"));
        overriddenRepos = Repositories.withConfig(overriddenConfig);
    }
    
    @Test
    public void testGetRepository() {
        assertRepository(repos.getRepository("One"), "One", "foobar", null, null);
        assertRepository(repos.getRepository("Two"), "Two", "foobar", "pietjepluk", "noencryptionfornow!");
    }
    
    @Test
    public void testGetSystemRepository() {
        assertRepository(repos.getSystemRepository(), "One", "foobar", null, null);
    }
    
    @Test
    public void testGetDeafultSystemRepository() {
        File dir = new File(CeylonConfig.getInstallDir(), "repo");
        assertRepository(defaultRepos.getSystemRepository(), "SYSTEM", dir.getAbsolutePath(), null, null);
    }
    
    @Test
    public void testGetOverriddenSystemRepository() {
        assertRepository(overriddenRepos.getSystemRepository(), "SYSTEM", "system", null, null);
    }
    
    @Test
    public void testGetOutputRepository() {
        assertRepository(repos.getOutputRepository(), "Two", "foobar", "pietjepluk", "noencryptionfornow!");
    }
    
    @Test
    public void testGetDefaultOutputRepository() {
        assertRepository(defaultRepos.getOutputRepository(), "LOCAL", "./modules", null, null);
    }
    
    @Test
    public void testGetOverriddenOutputRepository() {
        assertRepository(overriddenRepos.getOutputRepository(), "LOCAL", "local", null, null);
    }
    
    @Test
    public void testGetCacheRepository() {
        assertRepository(repos.getCacheRepository(), "Three", "foobar", null, null);
    }
    
    @Test
    public void testGetDefaultCacheRepository() {
        File dir = defaultRepos.getCacheRepoDir();
        assertRepository(defaultRepos.getCacheRepository(), "CACHE", dir.getAbsolutePath(), null, null);
    }
    
    @Test
    public void testGetOverriddenCacheRepository() {
        assertRepository(overriddenRepos.getCacheRepository(), "CACHE", "cache", null, null);
    }
    
    @Test
    public void testGetLocalLookupRepositories() {
        Repository[] lookup = repos.getLocalLookupRepositories();
        Assert.assertTrue(lookup.length == 4);
        assertRepository(lookup[0], "Two", "foobar", "pietjepluk", "noencryptionfornow!");
        assertRepository(lookup[1], "Three", "foobar", null, null);
        assertRepository(lookup[2], "Four", "foobar", null, null);
        assertRepository(lookup[3], "%lookup-4", "foobar", null, null);
    }
    
    @Test
    public void testGetDefaultLocalLookupRepositories() {
        Repository[] lookup = defaultRepos.getLocalLookupRepositories();
        Assert.assertTrue(lookup.length == 1);
        assertRepository(lookup[0], "LOCAL", "./modules", null, null);
    }
    
    @Test
    public void testGetDefaultGlobalLookupRepositories() {
        Repository[] lookup = defaultRepos.getGlobalLookupRepositories();
        Assert.assertTrue(lookup.length == 2);
        File userDir = defaultRepos.getUserRepoDir();
        assertRepository(lookup[0], "USER", userDir.getAbsolutePath(), null, null);
        assertRepository(lookup[1], "REMOTE", Repositories.REPO_URL_CEYLON, null, null);
    }
    
    @Test
    public void testGetOverriddenLocalLookupRepositories() {
        Repository[] lookup = overriddenRepos.getLocalLookupRepositories();
        Assert.assertTrue(lookup.length == 1);
        assertRepository(lookup[0], "LOCAL", "local", null, null);
    }
    
    @Test
    public void testGetOverriddenGlobalLookupRepositories() {
        Repository[] lookup = overriddenRepos.getGlobalLookupRepositories();
        Assert.assertTrue(lookup.length == 2);
        assertRepository(lookup[0], "USER", "user", null, null);
        assertRepository(lookup[1], "REMOTE", "http://remote", null, null);
    }
    
    private void assertRepository(Repository repo, String name, String url, String user, String password) {
        Assert.assertNotNull(repo);
        Assert.assertEquals(name, repo.getName());
        Assert.assertEquals(url, repo.getUrl());
        Credentials credentials = repo.getCredentials();
        Assert.assertEquals(user, credentials != null ? credentials.getUser() : null);
        Assert.assertEquals(password, credentials != null ? credentials.getPassword() : null);
    }
}
